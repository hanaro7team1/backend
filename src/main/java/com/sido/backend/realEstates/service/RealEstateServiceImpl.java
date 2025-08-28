package com.sido.backend.realEstates.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.sido.backend.common.dto.PageResponseDTO;
import com.sido.backend.realEstates.dto.RealEstateDTO;
import com.sido.backend.realEstates.dto.RealEstateResponseDetailDTO;
import com.sido.backend.realEstates.entity.RealEstates;
import com.sido.backend.realEstates.repository.RealEstatesRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class RealEstateServiceImpl implements RealEstateService {

	private final RealEstatesRepository realEstatesRepository;

	@Override
	public PageResponseDTO<RealEstateDTO, RealEstates> getRealEstateList(int page, int listSize) {
		Slice<RealEstates> lists = realEstatesRepository.findAll(
			PageRequest.of(page - 1, listSize, Sort.by(Sort.Order.desc("id"))));
		return new PageResponseDTO<>(lists, RealEstateServiceImpl::toDTO);
	}

	@Override
	public RealEstateResponseDetailDTO getRealEstateDetail(Long id) {
		RealEstates realEstate = realEstatesRepository.findById(id).orElseThrow(
			() -> new EntityNotFoundException("해당 매물을 찾을 수 없습니다.")
		);
		return toDetailDTO(realEstate);
	}

	public static RealEstateDTO toDTO(RealEstates realEstate) {
		return RealEstateDTO.builder()
			.id(realEstate.getId())
			.address(realEstate.getAddress())
			.price(realEstate.getPrice())
			.capacity(realEstate.getCapacity())
			.area(realEstate.getArea())
			.tradeType(realEstate.getTradeType())
			.build();
	}

	public static RealEstateResponseDetailDTO toDetailDTO(RealEstates realEstate) {
		List<String> imageUrls = realEstate.getImages().stream()
			.map(image -> image.getSavedir())
			.collect(Collectors.toList());

		return RealEstateResponseDetailDTO.builder()
			.id(realEstate.getId())
			.address(realEstate.getAddress())
			.price(realEstate.getPrice())
			.capacity(realEstate.getCapacity())
			.area(realEstate.getArea())
			.tradeType(realEstate.getTradeType())
			.description(realEstate.getDescription())
			.areaSize(realEstate.getAreaSize())
			.roomCount(realEstate.getRoomCount())
			.house(realEstate.getHouse())
			.imageUrls(imageUrls)
			.build();
	}
}
