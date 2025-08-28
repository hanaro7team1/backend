package com.sido.backend.realEstates.service;

import com.sido.backend.common.dto.PageResponseDTO;
import com.sido.backend.realEstates.dto.RealEstateDTO;
import com.sido.backend.realEstates.dto.RealEstateResponseDetailDTO;
import com.sido.backend.realEstates.entity.RealEstates;

public interface RealEstateService {
    PageResponseDTO<RealEstateDTO, RealEstates> getRealEstateList(int page, int listSize);
    RealEstateResponseDetailDTO getRealEstateDetail(Long id);
}
