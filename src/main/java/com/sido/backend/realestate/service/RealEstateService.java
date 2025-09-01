package com.sido.backend.realestate.service;

import com.sido.backend.common.dto.PageResponseDTO;
import com.sido.backend.realestate.dto.RealEstateDetailResponseDTO;
import com.sido.backend.realestate.dto.RealEstateResponseDTO;
import com.sido.backend.realestate.entity.RealEstate;

public interface RealEstateService {
	PageResponseDTO<RealEstateResponseDTO, RealEstate> getRealEstateList(int page, int listSize);

	RealEstateDetailResponseDTO getRealEstateDetail(Long id);
}
