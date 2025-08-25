package com.sido.backend.house.service;

import com.sido.backend.house.dto.HouseRequestDTO;
import com.sido.backend.house.dto.HouseResponseDateDTO;
import com.sido.backend.house.dto.HouseResponseRegisterDTO;

public interface HouseService {
	HouseResponseRegisterDTO addHouse(HouseRequestDTO requestDetailDTO);

	HouseResponseDateDTO addHouseDate(HouseRequestDTO requestDetailDTO);
}
