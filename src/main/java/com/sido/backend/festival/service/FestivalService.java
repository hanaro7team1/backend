package com.sido.backend.festival.service;

import com.sido.backend.common.dto.PageResponseDTO;
import com.sido.backend.festival.dto.FestivalResponseDTO;
import com.sido.backend.festival.dto.FestivalResponseDetailDTO;
import com.sido.backend.festival.dto.FestivalRequestDTO;
import com.sido.backend.festival.entity.Festival;

public interface FestivalService {
	PageResponseDTO<FestivalResponseDTO, Festival> getFestivalList(int page, int pageSize);

	FestivalResponseDetailDTO getServiceDetail(Long id);

	FestivalResponseDetailDTO addFestival(FestivalRequestDTO requestDTO);

	FestivalResponseDetailDTO editFestival(FestivalRequestDTO requestDTO);

	void removeFestival(Long id);
}
