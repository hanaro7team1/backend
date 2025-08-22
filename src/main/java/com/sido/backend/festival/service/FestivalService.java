package com.sido.backend.festival.service;

import java.util.List;

import com.sido.backend.festival.dto.FestivalResponseDTO;
import com.sido.backend.festival.dto.FestivalResponseDetailDTO;
import com.sido.backend.festival.dto.FestivalRequestDTO;

public interface FestivalService {
	List<FestivalResponseDTO> getFestivalList(int page, int pageSize);

	FestivalResponseDetailDTO getServiceDetail(Long id);

	FestivalResponseDetailDTO addFestival(FestivalRequestDTO requestDTO);

	FestivalResponseDetailDTO editFestival(FestivalRequestDTO requestDTO);

	void removeFestival(Long id);
}
