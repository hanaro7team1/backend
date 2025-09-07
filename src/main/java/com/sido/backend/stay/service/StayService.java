package com.sido.backend.stay.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Pageable;

import com.sido.backend.common.dto.PageResponseDTO;
import com.sido.backend.stay.dto.AvailDatesDTO;
import com.sido.backend.stay.dto.OpenAndReservedDatesDTO;
import com.sido.backend.stay.dto.StayCreateDTO;
import com.sido.backend.stay.dto.StayDeleteDTO;
import com.sido.backend.stay.dto.StayResponseDTO;
import com.sido.backend.stay.dto.StayResponseDetailDTO;
import com.sido.backend.stay.dto.StayResrvStatus;
import com.sido.backend.stay.dto.StayUpdateDTO;
import com.sido.backend.stay.entity.Stay;

public interface StayService {
	PageResponseDTO<StayResponseDTO, Stay> getStays(int page, int listSize,
		boolean isHomestay, String address, LocalDate startDate, LocalDate endDate, Integer capacity);

	PageResponseDTO<StayResponseDTO, Stay> getStaysByHost(
		Long memberId,
		Pageable pageable,
		StayResrvStatus statusFilter
	);

	StayResponseDetailDTO addStay(long memberId, StayCreateDTO stayCreateDTO);

	StayUpdateDTO editStay(long stayId, long memberId, StayUpdateDTO stayDTO);

	AvailDatesDTO getAvailableDates(Long stayId);

	StayResponseDetailDTO getStayDetail(Long stayId, LocalDate startDate, LocalDate endDate);

	StayDeleteDTO deleteStay(Long memberId, Long stayId);

	OpenAndReservedDatesDTO getOpenAndReservedDates(Long stayId);

	OpenAndReservedDatesDTO updateOpenDates(Long stayId, List<LocalDate> dates);
}
