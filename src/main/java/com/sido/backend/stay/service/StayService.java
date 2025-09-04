package com.sido.backend.stay.service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

import com.sido.backend.common.dto.PageResponseDTO;
import com.sido.backend.stay.dto.AvailDatesDTO;
import com.sido.backend.stay.dto.OpenAndReservedDatesDTO;
import com.sido.backend.stay.dto.StayCreateDTO;
import com.sido.backend.stay.dto.StayResponseDTO;
import com.sido.backend.stay.dto.StayResponseDetailDTO;
import com.sido.backend.stay.dto.StayUpdateDTO;
import com.sido.backend.stay.entity.Stay;

public interface StayService {
	PageResponseDTO<StayResponseDTO, Stay> getStays(int page, int listSize,
		boolean isHomestay, String address, LocalDate startDate, LocalDate endDate, Integer capacity);

	StayResponseDetailDTO addStay(long memberId, StayCreateDTO stayCreateDTO);

	StayUpdateDTO editStay(long stayId, long memberId, StayUpdateDTO stayDTO);

	AvailDatesDTO getAvailableDatesByMonth(Long stayId, YearMonth yearMonth);

	StayResponseDetailDTO getStayDetail(Long stayId);

	void deleteStay(Long stayId);

	OpenAndReservedDatesDTO getOpenAndReservedDatesByMonth(Long stayId, YearMonth yearMonth);

	OpenAndReservedDatesDTO updateOpenDates(Long stayId, List<LocalDate> dates);
}
