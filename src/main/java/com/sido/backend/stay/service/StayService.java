package com.sido.backend.stay.service;

import com.sido.backend.stay.dto.StayRequestDTO;
import com.sido.backend.stay.dto.StayRegisterDTO;

public interface StayService {
	StayRegisterDTO addHouse(StayRequestDTO requestDetailDTO);


}
