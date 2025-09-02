package com.sido.backend.reservation.controller;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.sido.backend.reservation.dto.ReservationConfirmRequestDTO;
import com.sido.backend.reservation.dto.ReservationConfirmResponseDTO;
import com.sido.backend.reservation.dto.ReservationCreateRequestDTO;
import com.sido.backend.reservation.dto.ReservationCreateResponseDTO;
import com.sido.backend.reservation.dto.ReservationDetailResponseDTO;
import com.sido.backend.reservation.service.ReservationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@Tag(name = "예약-도시 시니어")
public class ReservationController {
	private final ReservationService reservationService;

	@Operation(summary = "예약하기", description = "ResrvStatus: PENDING으로")
	@PostMapping("/api/stays/{stayId}/reservations")
	public ResponseEntity<ReservationCreateResponseDTO> createReservation(
		@AuthenticationPrincipal(expression = "memberId") Long memberId, @PathVariable Long stayId,
		@Valid @RequestBody ReservationCreateRequestDTO createRequest) {
		ReservationCreateResponseDTO createResponse = reservationService.createReservation(memberId, stayId,
			createRequest);
		URI location = URI.create("/api/reservations/" + createResponse.reservationId()); // 생성된 리소스의 URI
		return ResponseEntity.created(location).body(createResponse);
	}

	@Operation(summary = "예약 확정", description = "ResrvStatus: RESERVED로")
	@PatchMapping("/api/reservations/{reservationId}/confirm")
	public ResponseEntity<ReservationConfirmResponseDTO> confirmReservation(
		@AuthenticationPrincipal(expression = "memberId") Long memberId, @PathVariable Long reservationId,
		@Valid @RequestBody ReservationConfirmRequestDTO confirmRequest) {
		ReservationConfirmResponseDTO confirmResponse = reservationService.confirmReservation(memberId,
			reservationId, confirmRequest);
		return ResponseEntity.ok().body(confirmResponse);
	}

	@Operation(summary = "예약 상세 조회")
	@GetMapping("/api/reservations/{reservationId}")
	public ResponseEntity<ReservationDetailResponseDTO> getReservationDetail(
		@AuthenticationPrincipal(expression = "memberId") Long memberId, @PathVariable Long reservationId) {
		ReservationDetailResponseDTO detailResponse = reservationService.getReservationDetail(memberId, reservationId);
		return ResponseEntity.ok().body(detailResponse);
  }
  
	@Operation(summary = "예약 취소", description = "ResrvStatus: CANCELLED로, VisitStatus: null로")
	@DeleteMapping("/api/reservations/{reservationId}")
	public ResponseEntity<Void> cancelReservation(
		@AuthenticationPrincipal(expression = "memberId") Long memberId, @PathVariable Long reservationId) {
		reservationService.cancelReservation(memberId, reservationId);
		return ResponseEntity.noContent().build();
	}
}
