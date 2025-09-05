package com.sido.backend.stay.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.sido.backend.realestate.entity.RealEstate;
import com.sido.backend.realestate.repository.RealEstateRepository;
import com.sido.backend.stay.dto.RegionResponseDTO;
import com.sido.backend.stay.utils.AddressRegionGrouper;

import lombok.RequiredArgsConstructor;

@Service("realEstateRegionService")
@RequiredArgsConstructor
public class RealEstateRegionServiceImpl implements RegionService {

	private final RealEstateRepository realEstateRepository;
	private final AddressRegionGrouper grouper;

	@Override
	public List<RegionResponseDTO> groupByProvince() {
		var addresses = realEstateRepository.findAll()
			.stream().map(RealEstate::getAddress).toList();
		return grouper.groupByProvinceFromAddresses(addresses);
	}
}