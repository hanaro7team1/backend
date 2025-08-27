package com.sido.backend.stay.service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

import org.springframework.stereotype.Service;

import com.sido.backend.member.entity.HostMember;
import com.sido.backend.member.repository.HostMemberRepository;
import com.sido.backend.stay.dto.AvailDatesDTO;
import com.sido.backend.stay.dto.StayCreateDTO;
import com.sido.backend.stay.dto.StayRequestDTO;
import com.sido.backend.stay.dto.StayResponseDetailDTO;
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
	public StayCreateDTO addStay(long memberId, StayRequestDTO requestDTO) {
		HostMember host = hostMemberRepository.findById(memberId).orElseThrow(
			() -> new EntityNotFoundException("해당 호스트를 찾을 수 없습니다.")
		);

		Stay stay = requestDTO.toEntity();
		stay.setHost(host);
		stayRepository.save(stay);

		return toRegisterDTO(stay);
	}

	@Override
	public StayUpdateDTO editStay(long stayId, long memberId, StayUpdateDTO stayDTO) {
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
		return StayUpdateDTO.builder()
			.capacity(stay.getCapacity())
			.areaSize(stay.getAreaSize())
			.description(stay.getDescription())
			.build();
	}

	private StayCreateDTO toRegisterDTO(Stay stay) {
		return StayCreateDTO.builder()
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