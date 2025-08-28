package com.sido.backend.stay.service;

import java.time.YearMonth;

import com.sido.backend.stay.dto.AvailDatesDTO;
import com.sido.backend.stay.dto.StayCreateDTO;
import com.sido.backend.stay.dto.StayResponseDetailDTO;
import com.sido.backend.stay.dto.StayUpdateDTO;

public interface StayService {
	StayResponseDetailDTO addStay(long memberId, StayCreateDTO stayCreateDTO);

	StayUpdateDTO editStay(long stayId, long memberId, StayUpdateDTO stayDTO);

	AvailDatesDTO getAvailableDatesByMonth(Long stayId, YearMonth yearMonth);

	StayResponseDetailDTO getStayDetail(Long stayId);

	void deleteStay(Long stayId);
}