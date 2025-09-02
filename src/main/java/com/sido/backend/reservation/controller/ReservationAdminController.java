package com.sido.backend.reservation.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sido.backend.reservation.dto.ReservationSummaryDTO;
import com.sido.backend.reservation.service.ReservationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/reservations")
@Tag(name = "예약-시골 관리자")
public class ReservationAdminController {
	private final ReservationService reservationService;

	@Operation(summary = "예약 현황 조회", description = "방문 전, 방문 중, 방문 완료 수")
	@GetMapping("/summary")
	public ResponseEntity<ReservationSummaryDTO> getReservationSummary(
		@AuthenticationPrincipal(expression = "memberId") Long memberId) {
		ReservationSummaryDTO reservationSummary = reservationService.getReservationSummary(memberId);
		return ResponseEntity.ok(reservationSummary);
	}

}
