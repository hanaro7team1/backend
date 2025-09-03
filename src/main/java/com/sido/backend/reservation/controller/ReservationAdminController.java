package com.sido.backend.reservation.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sido.backend.common.dto.PageResponseDTO;
import com.sido.backend.reservation.dto.ReservationListFilter;
import com.sido.backend.reservation.dto.ReservationListItemDTO;
import com.sido.backend.reservation.dto.ReservationOverviewDTO;
import com.sido.backend.reservation.entity.Reservation;
import com.sido.backend.reservation.service.ReservationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/reservations")
@Tag(name = "예약-Admin")
public class ReservationAdminController {
	private final ReservationService reservationService;

	@Operation(summary = "예약 현황 조회", description = "방문 전, 방문 중, 방문 완료 수")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/overview")
	public ResponseEntity<ReservationOverviewDTO> getReservationOverview(
		@AuthenticationPrincipal(expression = "memberId") Long memberId) {
		ReservationOverviewDTO reservationSummary = reservationService.getReservationOverview(memberId);
		return ResponseEntity.ok(reservationSummary);
	}

	@Operation(summary = "예약 목록 조회 (시골 관리자)")
	@ApiResponse(content = @Content(
		examples = @ExampleObject(
			value = """
				{
					"reservationId": 1,
					"imageUrl": "https://example.com/image.jpg",
					"title": "가람마을 사랑방 1호",
					"viewStatus": "방문 전"
					"dDay": 5,
					"startDate": "2025-10-01",
					"endDate": "2025-10-05"
				}
				"""
		)
	))
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping
	public ResponseEntity<PageResponseDTO<ReservationListItemDTO, Reservation>> getAdminReservationList(
		@AuthenticationPrincipal(expression = "memberId") Long memberId,
		@RequestParam(defaultValue = "1") int page,
		@RequestParam(defaultValue = "10") int listSize,
		@RequestParam(defaultValue = "ALL") ReservationListFilter filter) {
		PageResponseDTO<ReservationListItemDTO, Reservation> reservationList =
			reservationService.getAdminReservationList(memberId, page, listSize, filter);
		return ResponseEntity.ok(reservationList);
	}
}
