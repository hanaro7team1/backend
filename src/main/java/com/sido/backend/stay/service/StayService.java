package com.sido.backend.stay.service;

import java.time.YearMonth;

import com.sido.backend.stay.dto.AvailDatesDTO;
import com.sido.backend.stay.dto.StayDTO;
import com.sido.backend.stay.dto.StayRegisterDTO;
import com.sido.backend.stay.dto.StayRequestDTO;
import com.sido.backend.stay.dto.StayResponseDetailDTO;

public interface StayService {
	StayRegisterDTO addStay(long memberId, StayRequestDTO requestDTO);

	StayDTO editStay(long stayId, long memberId, StayDTO stayDTO);

	AvailDatesDTO getAvailableDatesByMonth(Long stayId, YearMonth yearMonth);

	StayResponseDetailDTO getStayDetail(Long stayId);

	void deleteStay(Long stayId);
}
