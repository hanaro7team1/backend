package com.sido.backend.festival.service;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public interface FestivalService {
	List<FestivalResponseDTO> getFestivalList(int page, int pageSize);

	FestivalResponseDetailDTO getServiceDetail(Long id);

	FestivalResponseDetailDTO addFestival(ServiceRequestDTO requestDTO);

	FestivalResponseDetailDTO editFestival(ServiceRequestDTO requestDTO);

	void removeFestival(Long id);
}
