package com.sido.backend.realestate.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sido.backend.common.dto.PageResponseDTO;
import com.sido.backend.realestate.dto.RealEstateDetailResponseDTO;
import com.sido.backend.realestate.dto.RealEstateResponseDTO;
import com.sido.backend.realestate.entity.RealEstate;
import com.sido.backend.realestate.service.RealEstateService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/real-estates")
@Tag(name = "매물")
public class RealEstateController {

	private final RealEstateService realEstateService;

	@Operation(description = "매물 목록 조회")
	@GetMapping
	public ResponseEntity<PageResponseDTO<RealEstateResponseDTO, RealEstate>> getRealEstateList(
		@RequestParam(defaultValue = "1") int page,
		@RequestParam(defaultValue = "10") int listSize,
		@RequestParam(required = false) String location,
		@RequestParam(required = false) String tradeType,
		@RequestParam(required = false) String price
	) {
		Integer minPrice = null;
		Integer maxPrice = null;

		if (price != null && !price.isBlank()) {
			String processedPrice = price.replace("만원", "").replaceAll("\\s", "");

			String[] parts = processedPrice.split("~|-"); // "~" 또는 "-"로 분리

			if (parts.length > 0 && !parts[0].isEmpty()) {
				try {
					minPrice = Integer.parseInt(parts[0]) * 10000;
				} catch (NumberFormatException e) {
					// 숫자가 아닌 경우 무시
				}
			}
			if (parts.length > 1 && !parts[1].isEmpty()) {
				try {
					maxPrice = Integer.parseInt(parts[1]) * 10000;
				} catch (NumberFormatException e) {
					// 숫자가 아닌 경우 무시
				}
			}
		}

		return ResponseEntity.ok(
			realEstateService.getRealEstateList(page, listSize, location, tradeType, minPrice, maxPrice));
	}

	@Operation(description = "매물 상세 조회")
	@GetMapping("/{realEstateId}")
	public ResponseEntity<RealEstateDetailResponseDTO> getRealEstateDetail(@PathVariable Long realEstateId) {
		return ResponseEntity.ok(realEstateService.getRealEstateDetail(realEstateId));
	}
}
