package com.sido.backend.stay.service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sido.backend.member.entity.HostMember;
import com.sido.backend.member.repository.HostMemberRepository;
import com.sido.backend.stay.dto.AvailDatesDTO;
import com.sido.backend.stay.dto.StayCreateDTO;
import com.sido.backend.stay.dto.StayResponseDetailDTO;
import com.sido.backend.stay.dto.StaySpecDTO;
import com.sido.backend.stay.dto.StayUpdateDTO;
import com.sido.backend.stay.entity.Stay;
import com.sido.backend.stay.repository.StayAvailDateRepository;
import com.sido.backend.stay.repository.StayRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class StayServiceImpl implements StayService {
	private final StayRepository stayRepository;
	private final StayAvailDateRepository stayAvailDateRepository;
	private final HostMemberRepository hostMemberRepository;

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
	public AvailDatesDTO getOpenDatesByMonth(Long stayId, YearMonth yearMonth) {
		stayRepository.findById(stayId).orElseThrow(
			() -> new EntityNotFoundException("해당 사랑방을 찾을 수 없습니다.")
		);

		MonthContext monthCtx = MonthContext.of(yearMonth);

		return buildOpenCalendar(stayId, monthCtx);
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

	private AvailDatesDTO buildOpenCalendar(Long stayId, MonthContext monthCtx) {
		List<LocalDate> dates;
		boolean hasPrev;
		boolean hasNext;

		if (monthCtx.isPast()) {
			// 과거 달: 항상 빈 목록, 좌측 이동 불가
			dates = List.of();
			hasPrev = false;
			hasNext = stayAvailDateRepository.existsByStayIdAndAvailableDateGreaterThanEqual(stayId, monthCtx.today);
		} else if (monthCtx.isCurrent()) {
			// 이번 달
			dates = stayAvailDateRepository.findOpenInRange(stayId, monthCtx.today, monthCtx.monthEndEx);
			hasPrev = false;
			hasNext = stayAvailDateRepository.existsByStayIdAndAvailableDateGreaterThanEqual(stayId,
				monthCtx.monthEndEx);
		} else {
			// 미래 달
			dates = stayAvailDateRepository.findOpenInRange(stayId, monthCtx.monthStart, monthCtx.monthEndEx);
			hasPrev = stayAvailDateRepository.existsByStayIdAndAvailableDateGreaterThanEqualAndAvailableDateLessThan(
				stayId, monthCtx.today, monthCtx.monthStart);
			hasNext = stayAvailDateRepository.existsByStayIdAndAvailableDateGreaterThanEqual(stayId,
				monthCtx.monthEndEx);
		}

		return AvailDatesDTO.builder()
			.yearMonth(monthCtx.target)
			.dates(dates)
			.hasPrev(hasPrev)
			.hasNext(hasNext)
			.build();
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
