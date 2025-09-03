package com.sido.backend.stay.service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sido.backend.common.dto.PageResponseDTO;
import com.sido.backend.member.entity.HostMember;
import com.sido.backend.member.repository.HostMemberRepository;
import com.sido.backend.stay.dto.AvailDatesDTO;
import com.sido.backend.stay.dto.OpenAndReservedDatesDTO;
import com.sido.backend.stay.dto.StayCreateDTO;
import com.sido.backend.stay.dto.StayResponseDTO;
import com.sido.backend.stay.dto.StayResponseDetailDTO;
import com.sido.backend.stay.dto.StayResrvStatus;
import com.sido.backend.stay.dto.StaySpecDTO;
import com.sido.backend.stay.dto.StayUpdateDTO;
import com.sido.backend.stay.entity.Stay;
import com.sido.backend.stay.entity.StayImage;
import com.sido.backend.stay.repository.StayAvailDateRepository;
import com.sido.backend.stay.repository.StayImageRepository;
import com.sido.backend.stay.repository.StayRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CopyObjectRequest;
import software.amazon.awssdk.services.s3.model.Delete;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.DeleteObjectsRequest;
import software.amazon.awssdk.services.s3.model.ObjectIdentifier;

@Slf4j
@RequiredArgsConstructor
@Service
public class StayServiceImpl implements StayService {
	private final S3Client s3;
	private final StayRepository stayRepository;
	private final StayImageRepository stayImageRepository;
	private final StayAvailDateRepository stayAvailDateRepository;
	private final HostMemberRepository hostMemberRepository;
	@Value("${app.s3.publicBaseUrl}")
	private String publicBaseUrl;
	@Value("${app.s3.bucket}")
	private String bucket;

	@Override
	public PageResponseDTO<StayResponseDTO, Stay> getStays(int page, int listSize,
		boolean isHomestay, String address, LocalDate startDate, LocalDate endDate, Integer capacity) {

		Slice<Object[]> stays = stayRepository.findStaysDynamically(
			isHomestay, address, startDate, endDate, capacity,
			PageRequest.of(page - 1, listSize, Sort.by(Sort.Order.desc("id")))
		);

		return new PageResponseDTO<>(stays, this::toResponseDTO);
	}

	@Override
	@Transactional
	public StayResponseDetailDTO addStay(long memberId, StayCreateDTO stayCreateDTO) {
		HostMember host = hostMemberRepository.findById(memberId).orElseThrow(
			() -> new EntityNotFoundException("해당 호스트를 찾을 수 없습니다.")
		);

		if (stayRepository.existsByAddressAndDetailAddress(
			stayCreateDTO.getAddress(),
			stayCreateDTO.getDetailAddress())) {
			throw new IllegalArgumentException("이미 등록된 주소입니다.");
		}

		// 사랑방 개수 증가 (HostMember 업데이트)
		host.incrementStayCount();

		Stay stay = stayCreateDTO.toEntity();
		stay.setHost(host);

		//{마을 이름} + 사랑방 + {사랑방 개수} + 호
		stay.setTitle(host.getVillageName() + " 사랑방 " + host.getStayCount() + "호");

		stayRepository.save(stay);

		//s3 버킷 temp에 있던 파일들 옮기는 과정
		List<String> tempKeys = Optional.ofNullable(stayCreateDTO.getS3Keys()).orElse(List.of());
		List<String> copiedKeys = new ArrayList<>();

		if (!tempKeys.isEmpty()) {
			String ym = YearMonth.now(ZoneId.of("Asia/Seoul")).format(DateTimeFormatter.ofPattern("yyyyMM"));

			try {
				for (String srcKey : tempKeys) {
					validateTempKey(srcKey);

					String destKey = buildFinalKey(stay.getId(), ym, srcKey);
					copyWithinBucket(srcKey, destKey);
					copiedKeys.add(destKey);

					StayImage img = new StayImage();
					img.setStay(stay);
					img.setS3Key(destKey);
					stayImageRepository.save(img);
				}
			} catch (Exception ex) {
				// 보상(가능한 한 복구)
				for (String k : copiedKeys)
					safeDelete(k);
				throw ex; // 트랜잭션 롤백 → Stay/StayImage 롤백
			}
			deleteMany(tempKeys);
		}
		return toResponseDetailDTO(stay);
	}

	@Override
	public StayUpdateDTO editStay(long stayId, long memberId, StayUpdateDTO stayDTO) {
		Stay stay = stayRepository.findById(stayId).orElseThrow(
			() -> new EntityNotFoundException("해당 사랑방을 찾을 수 없습니다.")
		);
		stay.setCapacity(stayDTO.capacity());
		stay.setAreaSize(stayDTO.areaSize());
		stay.setDescription(stayDTO.description());

		return toEditDTO(stayRepository.save(stay));
	}

	@Override
	public AvailDatesDTO getAvailableDatesByMonth(Long stayId, YearMonth yearMonth) {
		stayRepository.findById(stayId).orElseThrow(
			() -> new EntityNotFoundException("해당 사랑방을 찾을 수 없습니다.")
		);

		MonthContext monthCtx = MonthContext.of(yearMonth);

		return buildAvailableCalendar(stayId, monthCtx);
	}

	@Override
	public StayResponseDetailDTO getStayDetail(Long stayId) {
		Stay stay = stayRepository.findById(stayId).orElseThrow(
			() -> new EntityNotFoundException("해당 사랑방을 찾을 수 없습니다.")
		);
		return toResponseDetailDTO(stay);
	}

	@Override
	public void deleteStay(Long stayId) {
		Stay stay = stayRepository.findById(stayId).orElseThrow(
			() -> new EntityNotFoundException("해당 사랑방을 찾을 수 없습니다.")
		);
		stay.setIsActive(false);
		stayRepository.save(stay);
	}

	@Override
	public OpenAndReservedDatesDTO getOpenAndReservedDatesByMonth(Long stayId, YearMonth yearMonth) {
		stayRepository.findById(stayId).orElseThrow(
			() -> new EntityNotFoundException("해당 사랑방을 찾을 수 없습니다.")
		);

		MonthContext monthCtx = MonthContext.of(yearMonth);

		return buildOpenAndReservedCalendar(stayId, monthCtx);
	}

	private StayResponseDTO toResponseDTO(Object[] tuple) {
		Stay stay = (Stay)tuple[0];
		StayResrvStatus status = (StayResrvStatus)tuple[1];
		String firstImageURL = publicBaseUrl + "/" + stay.getImages().getFirst().getS3Key();

		return StayResponseDTO.builder()
			.id(stay.getId())
			.title(stay.getTitle())
			.address(stay.getAddress())
			.isHomestay(stay.getIsHomestay())
			.stayResrvStatus(status)
			.imageURL(firstImageURL)
			.build();
	}

	private StayResponseDetailDTO toResponseDetailDTO(Stay stay) {
		StayResponseDetailDTO.StayResponseDetailDTOBuilder builder = StayResponseDetailDTO.builder()
			.id(stay.getId())
			.title(stay.getTitle())
			.address(stay.getAddress())
			.detailAddress(stay.getDetailAddress())
			.capacity(stay.getCapacity())
			.areaSize(stay.getAreaSize())
			.description(stay.getDescription())
			.isHomestay(stay.getIsHomestay());

		if (!stay.getIsActive()) {
			builder.isActiveMsg("해당 사랑방은 예약이 닫힌 상태입니다.");
		}

		// StayImage → DTO 변환
		List<String> imageUrls = stay.getImages().stream()
			.map(img ->
				publicBaseUrl + "/" + img.getS3Key() // URL
			)
			.toList();

		builder.images(imageUrls);

		return builder.build();
	}

	private StayUpdateDTO toEditDTO(Stay stay) {
		StaySpecDTO spec = new StaySpecDTO(
			stay.getCapacity(),
			stay.getAreaSize(),
			stay.getDescription()
		);

		return StayUpdateDTO.builder()
			.staySpec(spec)
			.build();
	}

	private AvailDatesDTO buildAvailableCalendar(Long stayId, MonthContext monthCtx) {
		List<LocalDate> dates;
		boolean hasPrev;
		boolean hasNext;

		if (monthCtx.isPast()) {
			// 과거 달: 항상 빈 목록, 좌측 이동 불가
			dates = List.of();
			hasPrev = false;
			hasNext = stayAvailDateRepository.countOpenAndUnreservedOnOrAfter(stayId, monthCtx.today) > 0;
		} else if (monthCtx.isCurrent()) {
			// 이번 달
			dates = stayAvailDateRepository.findOpenAndUnreservedInRange(stayId, monthCtx.today, monthCtx.monthEndEx);
			hasPrev = false;
			hasNext = stayAvailDateRepository.countOpenAndUnreservedOnOrAfter(stayId, monthCtx.monthEndEx) > 0;
		} else {
			// 미래 달
			dates = stayAvailDateRepository.findOpenAndUnreservedInRange(stayId, monthCtx.monthStart,
				monthCtx.monthEndEx);
			hasPrev =
				stayAvailDateRepository.countOpenAndUnreservedInRange(stayId, monthCtx.today, monthCtx.monthStart) > 0;
			hasNext = stayAvailDateRepository.countOpenAndUnreservedOnOrAfter(stayId, monthCtx.monthEndEx) > 0;
		}

		return AvailDatesDTO.builder()
			.yearMonth(monthCtx.target)
			.dates(dates)
			.hasPrev(hasPrev)
			.hasNext(hasNext)
			.build();
	}

	private OpenAndReservedDatesDTO buildOpenAndReservedCalendar(Long stayId, MonthContext monthCtx) {
		List<LocalDate> openDates;
		boolean hasOpenPrev;
		boolean hasOpenNext;

		if (monthCtx.isPast()) {
			// 과거 달: 항상 빈 목록, 좌측 이동 불가
			openDates = List.of();
			hasOpenPrev = false;
			hasOpenNext = stayAvailDateRepository.existsByStayIdAndAvailableDateGreaterThanEqual(stayId,
				monthCtx.today);
		} else if (monthCtx.isCurrent()) {
			// 이번 달
			openDates = stayAvailDateRepository.findOpenInRange(stayId, monthCtx.today, monthCtx.monthEndEx);
			hasOpenPrev = false;
			hasOpenNext = stayAvailDateRepository.existsByStayIdAndAvailableDateGreaterThanEqual(stayId,
				monthCtx.monthEndEx);
		} else {
			// 미래 달
			openDates = stayAvailDateRepository.findOpenInRange(stayId, monthCtx.monthStart, monthCtx.monthEndEx);
			hasOpenPrev = stayAvailDateRepository.existsByStayIdAndAvailableDateGreaterThanEqualAndAvailableDateLessThan(
				stayId, monthCtx.today, monthCtx.monthStart);
			hasOpenNext = stayAvailDateRepository.existsByStayIdAndAvailableDateGreaterThanEqual(stayId,
				monthCtx.monthEndEx);
		}

		List<LocalDate> reservedDates = stayAvailDateRepository.findOpenAndReservedInRange(stayId, monthCtx.monthStart,
			monthCtx.monthEndEx);
		boolean hasReservedPrev = stayAvailDateRepository.countOpenAndReservedBefore(stayId, monthCtx.monthStart) > 0;
		boolean hasReservedNext =
			stayAvailDateRepository.countOpenAndReservedOnOrAfter(stayId, monthCtx.monthEndEx) > 0;

		return OpenAndReservedDatesDTO.builder()
			.yearMonth(monthCtx.target)
			.openDates(openDates)
			.hasOpenPrev(hasOpenPrev)
			.hasOpenNext(hasOpenNext)
			.reservedDates(reservedDates)
			.hasReservedPrev(hasReservedPrev)
			.hasReservedNext(hasReservedNext)
			.build();
	}

	//s3 관련 함수들
	private void validateTempKey(String key) {
		if (key == null || !key.startsWith("temp/") || key.contains("..")) {
			throw new IllegalArgumentException("잘못된 이미지 키");
		}
	}

	private void copyWithinBucket(String srcKey, String destKey) {
		s3.copyObject(CopyObjectRequest.builder()
			.sourceBucket(bucket)
			.sourceKey(srcKey)
			.destinationBucket(bucket)
			.destinationKey(destKey)
			.build());
	}

	private String buildFinalKey(Long stayId, String yyyyMM, String srcKey) {
		// srcKey 예: temp/202509/abc-uuid.jpg
		String filename = srcKey.substring(srcKey.lastIndexOf('/') + 1); // abc-uuid.jpg
		return "stays/%d/%s/%s".formatted(stayId, yyyyMM, filename);
	}

	private void safeDelete(String key) {
		try {
			s3.deleteObject(DeleteObjectRequest.builder().bucket(bucket).key(key).build());
		} catch (Exception ignore) {
		}
	}

	private void deleteMany(List<String> keys) {
		if (keys.isEmpty())
			return;
		s3.deleteObjects(DeleteObjectsRequest.builder()
			.bucket(bucket)
			.delete(Delete.builder()
				.objects(keys.stream()
					.map(k -> ObjectIdentifier.builder().key(k).build())
					.toList())
				.build())
			.build());
	}

	private record MonthContext(
		LocalDate today,
		YearMonth nowYM,
		YearMonth target,
		LocalDate monthStart,
		LocalDate monthEndEx
	) {
		static MonthContext of(YearMonth input) {
			LocalDate today = LocalDate.now();
			YearMonth nowYM = YearMonth.from(today);
			YearMonth target = (input == null) ? nowYM : input; // null이면 이번달

			LocalDate monthStart = target.atDay(1);
			LocalDate monthEndEx = target.plusMonths(1).atDay(1); // [monthStart, monthEndEx)

			return new MonthContext(today, nowYM, target, monthStart, monthEndEx);
		}

		boolean isPast() {
			return target.isBefore(nowYM);
		}

		boolean isCurrent() {
			return target.equals(nowYM);
		}
	}
}
