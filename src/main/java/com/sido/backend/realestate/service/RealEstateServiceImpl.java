package com.sido.backend.realestate.service;

import java.text.DecimalFormat;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
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

	@Value("${app.s3.publicBaseUrl}")
	private String publicBaseUrl;

	@Override
	public PageResponseDTO<RealEstateResponseDTO, RealEstate> getRealEstateList(int page, int listSize, String address,
		String tradeType, Long minPrice, Long maxPrice) {
		Slice<RealEstate> lists = realEstatesRepository.findRealEstatesDynamically(
			address, tradeType, minPrice, maxPrice, PageRequest.of(page - 1, listSize, Sort.by(Sort.Order.desc("id"))));
		return new PageResponseDTO<>(lists, this::toDTO);
	}

	@Override
	public RealEstateDetailResponseDTO getRealEstateDetail(Long id) {
		RealEstate realEstate = realEstatesRepository.findById(id).orElseThrow(
			() -> new EntityNotFoundException("해당 매물을 찾을 수 없습니다.")
		);
		return toDetailDTO(realEstate);
	}

	public RealEstateResponseDTO toDTO(RealEstate realEstate) {
		String imageUrl = null;
		if (realEstate.getImages() != null && !realEstate.getImages().isEmpty()) {
			imageUrl = publicBaseUrl + "/" + realEstate.getImages().getFirst().getS3Key();
		}

		return RealEstateResponseDTO.builder()
			.id(realEstate.getId())
			.location(realEstate.getLocation())
			.price(formatPrice(realEstate.getPrice()))
			.tradeType(realEstate.getTradeType())
			.imageUrl(imageUrl)
			.build();
	}

	public RealEstateDetailResponseDTO toDetailDTO(RealEstate realEstate) {
		List<String> imageUrls = realEstate.getImages().stream()
			.map(image -> publicBaseUrl + "/" + image.getS3Key())
			.collect(Collectors.toList());

		return RealEstateDetailResponseDTO.builder()
			.id(realEstate.getId())
			.location(realEstate.getLocation())
			.price(formatPrice(realEstate.getPrice()))
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

	private String formatPrice(Long price) {
		if (price == null) {
			return null;
		}

		if (price < 10000) {
			return new DecimalFormat("#,###").format(price) + "원";
		}

		long man = price / 10000;
		if (man < 10000) {
			return new DecimalFormat("#,###").format(man) + "만원";
		} else {
			long eok = man / 10000;
			long remainder = man % 10000;
			if (remainder == 0) {
				return new DecimalFormat("#,###").format(eok) + "억원";
			} else {
				return new DecimalFormat("#,###").format(eok) + "억 " + new DecimalFormat("#,###").format(remainder)
					+ "만원";
			}
		}
	}
}
