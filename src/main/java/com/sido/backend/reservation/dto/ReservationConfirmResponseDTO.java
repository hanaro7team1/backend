package com.sido.backend.reservation.dto;

import java.time.LocalDateTime;

import com.sido.backend.reservation.entity.ResrvStatus;
import com.sido.backend.reservation.entity.VisitStatus;

public record ReservationConfirmResponseDTO(
	Long reservationId,

	Long stayId,

	ResrvStatus resrvStatus,

	VisitStatus visitStatus,

	Long dDay,

	LocalDateTime reservedAt
) {
}
