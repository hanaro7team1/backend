package com.sido.backend.stay.service;

import org.springframework.stereotype.Service;

import com.sido.backend.stay.dto.StayRequestDTO;
import com.sido.backend.stay.dto.StayRegisterDTO;
import com.sido.backend.stay.repository.StayRepository;

@Service
public class StayServiceImpl implements StayService {
	private final StayRepository repository;

	public StayServiceImpl(StayRepository repository) {
		this.repository = repository;
	}

	@Override
	public StayRegisterDTO addHouse(StayRequestDTO requestDTO) {
		return null;
	}

}
