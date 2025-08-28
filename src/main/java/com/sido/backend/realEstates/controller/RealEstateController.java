package com.sido.backend.realEstates.controller;

import com.sido.backend.common.dto.PageResponseDTO;
import com.sido.backend.realEstates.dto.RealEstateDTO;
import com.sido.backend.realEstates.dto.RealEstateResponseDetailDTO;
import com.sido.backend.realEstates.entity.RealEstates;
import com.sido.backend.realEstates.service.RealEstateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/real-estates")
@Tag(name = "매물")
public class RealEstateController {

    private final RealEstateService realEstateService;

    @Operation(description = "매물 목록 조회")
    @GetMapping
    public ResponseEntity<PageResponseDTO<RealEstateDTO, RealEstates>> getRealEstateList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int listSize) {
        return ResponseEntity.ok(realEstateService.getRealEstateList(page, listSize));
    }

    @Operation(description = "매물 상세 조회")
    @GetMapping("/{id}")
    public ResponseEntity<RealEstateResponseDetailDTO> getRealEstateDetail(@PathVariable Long id) {
        return ResponseEntity.ok(realEstateService.getRealEstateDetail(id));
    }
}
