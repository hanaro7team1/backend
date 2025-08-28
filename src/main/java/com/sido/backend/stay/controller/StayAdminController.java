package com.sido.backend.stay.controller;

import java.time.YearMonth;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sido.backend.stay.dto.AvailDatesDTO;
import com.sido.backend.stay.dto.StayCreateDTO;
import com.sido.backend.stay.dto.StayResponseDetailDTO;
import com.sido.backend.stay.dto.StayUpdateDTO;
import com.sido.backend.stay.service.StayService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin/stays")
@Tag(name = "사랑방-Admin")
@RequiredArgsConstructor
public class StayAdminController {
	private final StayService stayService;

	@Operation(description = "등록")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PostMapping
	public ResponseEntity<StayResponseDetailDTO> addStay(
		@AuthenticationPrincipal(expression = "memberId") Long memberId,
		@Valid @RequestBody StayCreateDTO stayCreateDTO) {
		return ResponseEntity.ok(stayService.addStay(memberId, stayCreateDTO));
	}

	@Operation(description = "수정")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PatchMapping({"/{stayId}"})
	public ResponseEntity<?> editStay(@PathVariable long stayId,
		@AuthenticationPrincipal(expression = "memberId") Long memberId, @Valid @RequestBody StayUpdateDTO stayDTO) {
		return ResponseEntity.ok(stayService.editStay(stayId, memberId, stayDTO));
	}

	@Operation(description = "삭제")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@DeleteMapping("/{stayId}")
	public ResponseEntity<Void> deleteStay(@PathVariable Long stayId) {
		stayService.deleteStay(stayId);
		return ResponseEntity.noContent().build();
	}

	@Operation(summary = "월별 오픈한 날짜 조회", description = "시골 관리자: 사랑방 목록 관리- 예약 가능 날짜 변경하기")
	@GetMapping("/{stayId}/open-dates")
	public ResponseEntity<?> getOpenDatesByMonth(@PathVariable Long stayId,
		@Schema(example = "2025-08") @DateTimeFormat(pattern = "yyyy-MM") @RequestParam(required = false)
		YearMonth month) {
		AvailDatesDTO availDatesDTO = stayService.getOpenDatesByMonth(stayId, month);
		return ResponseEntity.ok(availDatesDTO);
	}
}
