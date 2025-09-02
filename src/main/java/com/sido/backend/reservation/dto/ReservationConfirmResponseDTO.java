package com.sido.backend.reservation.dto;

import com.fasterxml.jackson.annotation.JsonUnwrapped;

public record ReservationConfirmResponseDTO(
	Long reservationId,

	Long stayId,

	@JsonUnwrapped
	ReservationCommonDTOs.ReservationStatusDTO status // resrvStatus, visitStatus, dDay, reservedAt
) {
}
