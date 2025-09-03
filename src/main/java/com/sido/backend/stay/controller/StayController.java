package com.sido.backend.stay.controller;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sido.backend.common.dto.PageResponseDTO;
import com.sido.backend.stay.dto.AvailDatesDTO;
import com.sido.backend.stay.dto.StayResponseDTO;
import com.sido.backend.stay.dto.StayResponseDetailDTO;
import com.sido.backend.stay.entity.Stay;
import com.sido.backend.stay.service.StayService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/stays")
@Tag(name = "사랑방-Open")
public class StayController {
	private final StayService stayService;

	@Operation(summary = "숙소 전체 조회")
	@GetMapping
	public ResponseEntity<PageResponseDTO<StayResponseDTO, Stay>> getStays(
		@RequestParam(required = false) String roomType,
		@RequestParam(required = false) String location,
		@RequestParam(required = false) String schedule,
		@RequestParam(required = false) Integer peopleCount,
		@RequestParam(defaultValue = "1") int page,
		@RequestParam(defaultValue = "15") int listSize) {
		boolean isHomestay = !"독립형".equals(roomType); // 기본값 = 하숙형

		LocalDate startDate = null;
		LocalDate endDate = null;

		if (schedule != null && !schedule.isBlank()) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy.MM.dd");
			String[] parts = schedule.split("-");
			startDate = LocalDate.parse(parts[0].trim(), formatter);
			endDate   = LocalDate.parse(parts[1].trim(), formatter);
		}
		PageResponseDTO<StayResponseDTO, Stay> pagedStaysDTO = stayService.getStays(page, listSize,
			isHomestay, location, startDate, endDate, peopleCount);

		return ResponseEntity.ok(pagedStaysDTO);
	}

	@Operation(summary = "월별 예약 가능 날짜 조회")
	@GetMapping("/{stayId}/available-dates")
	public ResponseEntity<AvailDatesDTO> getAvailableDatesByMonth(@PathVariable("stayId") Long stayId,
		@Schema(example = "2025-08") @DateTimeFormat(pattern = "yyyy-MM") @RequestParam(required = false)
		YearMonth month) {
		AvailDatesDTO availDatesDTO = stayService.getAvailableDatesByMonth(stayId, month);
		return ResponseEntity.ok(availDatesDTO);
	}

	@Operation(summary = "숙소 상세 조회")
	@GetMapping("/{stayId}")
	public ResponseEntity<StayResponseDetailDTO> getStayDetail(@PathVariable("stayId") Long stayId) {
		StayResponseDetailDTO stayDetail = stayService.getStayDetail(stayId);
		return ResponseEntity.ok(stayDetail);
	}
}
