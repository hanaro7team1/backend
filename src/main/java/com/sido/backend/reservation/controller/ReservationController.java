package com.sido.backend.reservation.controller;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.sido.backend.reservation.dto.ReservationDTO;
import com.sido.backend.reservation.dto.ReservationResponseDTO;
import com.sido.backend.reservation.service.ReservationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@Tag(name = "예약")
public class ReservationController {
	private final ReservationService reservationService;

	@Operation(summary = "사랑방 상세 - 예약하기 버튼", description = "ResrvStatus: PENDING")
	@PostMapping("/api/stays/{stayId}/reservations")
	public ResponseEntity<?> createReservation(@PathVariable long stayId,
		@AuthenticationPrincipal(expression = "memberId") Long memberId,
		@RequestBody ReservationDTO reservationDTO) {
		ReservationResponseDTO responseDTO = reservationService.createReservation(stayId, memberId, reservationDTO);
		URI location = URI.create("/api/reservations/" + responseDTO.getReservationId() + "/confirm");
		// 201 응답 : 성공적으로 리소스 생성되었다는 의미
		// 생성된 자원에 접근할 수 있도록 식별자가 포함된 URI 반환하도록 created(URI)
		return ResponseEntity.created(location).body(responseDTO);
	}

	@Operation(summary = "예약하기 - 예약 확정 버튼 -> 예", description = "ResrvStatus: RESERVED")
	@PatchMapping("/api/reservations/{reservationId}/confirm")
	public ResponseEntity<?> confirmReservation(@PathVariable long reservationId,
		@AuthenticationPrincipal(expression = "memberId") Long memberId) {
		ReservationResponseDTO responseDTO = reservationService.confirmReservation(reservationId, memberId);
		return ResponseEntity.ok(responseDTO);
	}
}
