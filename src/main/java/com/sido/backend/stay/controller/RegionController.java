package com.sido.backend.stay.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sido.backend.stay.dto.RegionResponseDTO;
import com.sido.backend.stay.service.RegionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/regions")
@Tag(name = "필터링용 주소 api")
public class RegionController {
	private final RegionService stayRegionService;
	private final RegionService realEstateRegionService;

	//Qualifier는 롬복 생성자가 복사 X 직접 생성자 작성
	public RegionController(
		@Qualifier("stayRegionService") RegionService stayRegionService,
		@Qualifier("realEstateRegionService") RegionService realEstateRegionService
	) {
		this.stayRegionService = stayRegionService;
		this.realEstateRegionService = realEstateRegionService;
	}

	@GetMapping("/stays")
	@Operation(description = "사랑방용 주소")
	public ResponseEntity<List<RegionResponseDTO>> fromStays(
	) {
		return ResponseEntity.ok(stayRegionService.groupByProvince());
	}

	@GetMapping("/real-estates")
	@Operation(description = "부동산용 주소")
	public ResponseEntity<List<RegionResponseDTO>> fromRealEstates() {
		return ResponseEntity.ok(realEstateRegionService.groupByProvince());
	}

}