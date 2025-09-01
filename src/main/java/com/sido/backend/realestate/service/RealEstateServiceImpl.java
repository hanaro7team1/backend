package com.sido.backend.realestate.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.sido.backend.common.dto.PageResponseDTO;
import com.sido.backend.realestate.dto.RealEstateDetailResponseDTO;
import com.sido.backend.realestate.dto.RealEstateResponseDTO;
import com.sido.backend.realestate.entity.RealEstate;
import com.sido.backend.realestate.repository.RealEstateRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class RealEstateServiceImpl implements RealEstateService {

	private final RealEstateRepository realEstatesRepository;

	@Override
	public PageResponseDTO<RealEstateResponseDTO, RealEstate> getRealEstateList(int page, int listSize, String address, String tradeType, Integer minPrice, Integer maxPrice) {
		Slice<RealEstate> lists = realEstatesRepository.findRealEstatesDynamically(
				address, tradeType, minPrice, maxPrice, PageRequest.of(page - 1, listSize, Sort.by(Sort.Order.desc("id"))));
		return new PageResponseDTO<>(lists, RealEstateServiceImpl::toDTO);
	}

	@Override
	public RealEstateDetailResponseDTO getRealEstateDetail(Long id) {
		RealEstate realEstate = realEstatesRepository.findById(id).orElseThrow(
			() -> new EntityNotFoundException("해당 매물을 찾을 수 없습니다.")
		);
		return toDetailDTO(realEstate);
	}

	public static RealEstateResponseDTO toDTO(RealEstate realEstate) {
		return RealEstateResponseDTO.builder()
			.id(realEstate.getId())
			.address(realEstate.getAddress())
			.price(realEstate.getPrice())
			.tradeType(realEstate.getTradeType())
			.build();
	}

	public static RealEstateDetailResponseDTO toDetailDTO(RealEstate realEstate) {
		List<String> imageUrls = realEstate.getImages().stream()
			.map(image -> image.getSavedir())
			.collect(Collectors.toList());

		return RealEstateDetailResponseDTO.builder()
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
