package com.sido.backend.stay.service;

import com.sido.backend.stay.dto.StayDTO;
import com.sido.backend.stay.dto.StayRequestDTO;
import com.sido.backend.stay.dto.StayRegisterDTO;

public interface StayService {
	StayRegisterDTO addStay(long memberId, StayRequestDTO requestDTO);

	StayDTO editStay(long stayId, long memberId, StayDTO stayDTO);
}
