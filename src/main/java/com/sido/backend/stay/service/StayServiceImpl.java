package com.sido.backend.stay.service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;


import org.springframework.stereotype.Service;

import com.sido.backend.member.entity.HostMember;
import com.sido.backend.member.repository.HostMemberRepository;
import com.sido.backend.stay.dto.AvailDatesDTO;
import com.sido.backend.stay.dto.StayDTO;
import com.sido.backend.stay.dto.StayResponseDetailDTO;
import com.sido.backend.stay.dto.StayRegisterDTO;
import com.sido.backend.stay.dto.StayRequestDTO;
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
	public StayRegisterDTO addStay(long memberId, StayRequestDTO requestDTO) {
		HostMember host = hostMemberRepository.findById(memberId).orElseThrow(
			() -> new EntityNotFoundException("해당 호스트를 찾을 수 없습니다.")
		);

		Stay stay = requestDTO.toEntity();
		stay.setHost(host);
		stayRepository.save(stay);

		return toRegisterDTO(stay);
	}

	@Override
	public StayDTO editStay(long stayId, long memberId, StayDTO stayDTO) {
		Stay stay = stayRepository.findById(stayId).orElseThrow(
			() -> new EntityNotFoundException("해당 사랑방을 찾을 수 없습니다.")
		);
		stay.setCapacity(stayDTO.getCapacity());
		stay.setAreaSize(stayDTO.getAreaSize());
		stay.setDescription(stayDTO.getDescription());

		return toEditDTO(stayRepository.save(stay));
	}

	@Override
	public AvailDatesDTO getAvailableDatesByMonth(Long stayId, YearMonth yearMonth) {
		stayRepository.findById(stayId).orElseThrow(
			() -> new EntityNotFoundException("해당 사랑방을 찾을 수 없습니다.")
		);

		YearMonth target = (yearMonth == null) ? YearMonth.from(LocalDate.now()) : yearMonth;
		LocalDate start = target.atDay(1);
		LocalDate endExclusive = target.plusMonths(1).atDay(1); // [start, end)

		List<LocalDate> availableDates = stayAvailDateRepository.findAvailableDatesInRange(stayId, start, endExclusive);
		boolean hasPrev = stayAvailDateRepository.existsByStayIdAndAvailableDateLessThanAndIsAvailableTrue(stayId,
			start);
		boolean hasNext = stayAvailDateRepository.existsByStayIdAndAvailableDateGreaterThanAndIsAvailableTrue(stayId,
			endExclusive.minusDays(1));

		return AvailDatesDTO.builder()
			.yearMonth(target)
			.dates(availableDates)
			.hasPrev(hasPrev)
			.hasNext(hasNext)
			.build();
	}

	@Override
	public StayResponseDetailDTO getStayDetail(Long stayId) {
		Stay stay = stayRepository.findById(stayId).orElseThrow(
			() -> new EntityNotFoundException("해당 사랑방을 찾을 수 없습니다.")
		);
		return toResponseDetailDTO(stay);
	}

	private StayResponseDetailDTO toResponseDetailDTO(Stay stay) {
		HostMember host = stay.getHost();
		List<String> imageUrls = stay.getImages().stream()
			.map(image -> image.getSaveUrl())
			.collect(Collectors.toList());

		return StayResponseDetailDTO.builder()
			.id(stay.getId())
			.title(stay.getTitle())
			.address(stay.getAddress())
			.capacity(stay.getCapacity())
			.areaSize(stay.getAreaSize())
			.description(stay.getDescription())
			.isHomestay(stay.getIsHomestay())
			.ownerName(stay.getOwnerName())
			.ownerPhone(stay.getOwnerPhone())
			.hostVillageName(host != null ? host.getVillageName() : null)
			.hostRegion(host != null ? host.getRegion() : null)
			.hostPhone(host != null ? host.getPhone() : null)
			.createdAt(stay.getCreatedAt())
			.updatedAt(stay.getUpdatedAt())
			.imageUrls(imageUrls)
			.build();
	}

	private StayDTO toEditDTO(Stay stay) {
		return StayDTO.builder()
			.capacity(stay.getCapacity())
			.areaSize(stay.getAreaSize())
			.description(stay.getDescription())
			.build();
	}

	private StayRegisterDTO toRegisterDTO(Stay stay) {
		return StayRegisterDTO.builder()
			.title(stay.getTitle())
			.address(stay.getAddress())
			.capacity(stay.getCapacity())
			.areaSize(stay.getAreaSize())
			.ownerName(stay.getOwnerName())
			.ownerPhone(stay.getOwnerPhone())
			.description(stay.getDescription())
			.build();
	}
}
