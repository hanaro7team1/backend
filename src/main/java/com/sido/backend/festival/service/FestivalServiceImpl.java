package com.sido.backend.festival.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.sido.backend.festival.dto.FestivalResponseDTO;
import com.sido.backend.festival.dto.FestivalResponseDetailDTO;
import com.sido.backend.festival.dto.FestivalServiceRequestDTO;

@Service
public class FestivalServiceImpl implements FestivalService{

	@Override
	public List<FestivalResponseDTO> getFestivalList(int page, int pageSize) {
		return List.of();
	}

	@Override
	public FestivalResponseDetailDTO getServiceDetail(Long id) {
		return null;
	}

	@Override
	public FestivalResponseDetailDTO addFestival(FestivalServiceRequestDTO requestDTO) {
		return null;
	}

	@Override
	public FestivalResponseDetailDTO editFestival(FestivalServiceRequestDTO requestDTO) {
		return null;
	}

	@Override
	public void removeFestival(Long id) {

	}
}
