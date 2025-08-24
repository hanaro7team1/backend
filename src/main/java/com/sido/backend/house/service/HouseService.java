package com.sido.backend.house.service;

import com.sido.backend.house.dto.HouseDetailRequestDTO;
import com.sido.backend.house.dto.HouseResponseDTO;

public interface HouseService {
	HouseResponseDTO addHouse(HouseDetailRequestDTO requestDetailDTO);
}
