package com.sido.backend.house.service;

import org.springframework.stereotype.Service;

import com.sido.backend.house.dto.HouseRequestDTO;
import com.sido.backend.house.dto.HouseResponseDateDTO;
import com.sido.backend.house.dto.HouseResponseRegisterDTO;
import com.sido.backend.house.repository.HouseRepository;

@Service
public class HouseServiceImpl implements HouseService {
	private final HouseRepository repository;

	public HouseServiceImpl(HouseRepository repository) {
		this.repository = repository;
	}

	@Override
	public HouseResponseRegisterDTO addHouse(HouseRequestDTO requestDTO) {
		return null;
	}

	@Override
	public HouseResponseDateDTO addHouseDate(HouseRequestDTO requestDetailDTO) {
		return null;
	}
}
