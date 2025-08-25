package com.sido.backend.stay.service;

import org.springframework.stereotype.Service;

import com.sido.backend.stay.dto.StayEditDTO;
import com.sido.backend.stay.dto.StayRequestDTO;
import com.sido.backend.stay.dto.StayRegisterDTO;
import com.sido.backend.stay.entity.Stay;
import com.sido.backend.stay.repository.StayRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class StayServiceImpl implements StayService {
	private final StayRepository repository;

	// public StayServiceImpl(StayRepository repository) {
	// 	this.repository = repository;
	// }

	@Override
	public StayRegisterDTO addStay(StayRequestDTO requestDTO) {
		Stay stay = toStayEntity(requestDTO);

		return toRegisterDTO(repository.save(Stay));
	}

	@Override
	public StayEditDTO editStay(StayRequestDTO requestDTO) {
		return null;
	}

	private StayRegisterDTO toRegisterDTO(Stay stay) {
		return StayRegisterDTO.builder()
			.isHomestay(true)
			.title(stay.getTitle())
			.address(stay.getAddress())
			.capacity(stay.getCapacity())
			.areaSize(stay.getAreaSize())
			.owner(stay.getOwner())
			.ownerPhone(stay.getOwnerPhone())
			.description(stay.getDescription())
			.build();
	}

	private static Stay toStayEntity(StayRequestDTO dto) {
		return Stay.builder()
			.isHomestay(true)
			.title(dto.getTitle())
			.address(dto.getAddress())
			.capacity(dto.getCapacity())
			.areaSize(dto.getAreaSize())
			.owner(dto.getOwner())
			.ownerPhone(dto.getOwnerPhone())
			.description(dto.getDescription())
			.build();
	}
}
