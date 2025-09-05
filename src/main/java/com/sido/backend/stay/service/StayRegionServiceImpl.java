package com.sido.backend.stay.service;

import java.util.List;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import com.sido.backend.stay.dto.RegionResponseDTO;
import com.sido.backend.stay.entity.Stay;
import com.sido.backend.stay.repository.StayRepository;
import com.sido.backend.stay.utils.AddressRegionGrouper;

import lombok.RequiredArgsConstructor;

@Primary
@Service("stayRegionService")
@RequiredArgsConstructor
public class StayRegionServiceImpl implements RegionService {

	private final StayRepository stayRepository;
	private final AddressRegionGrouper grouper;

	@Override
	public List<RegionResponseDTO> groupByProvince() {
		var addresses = stayRepository.findAll()
			.stream().map(Stay::getAddress).toList();
		return grouper.groupByProvinceFromAddresses(addresses);
	}
}