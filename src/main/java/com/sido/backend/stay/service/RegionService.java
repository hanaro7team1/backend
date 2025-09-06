package com.sido.backend.stay.service;

import java.util.List;

import com.sido.backend.stay.dto.RegionResponseDTO;

public interface RegionService {
	List<RegionResponseDTO> groupByProvince();
}