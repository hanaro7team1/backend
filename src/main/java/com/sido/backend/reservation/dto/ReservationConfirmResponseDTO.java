package com.sido.backend.reservation.dto;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.sido.backend.reservation.dto.ReservationCommonDTOs.ReservationStatusDTO;

public record ReservationConfirmResponseDTO(
	Long reservationId,

	Long stayId,

	@JsonUnwrapped
	ReservationStatusDTO status // resrvStatus, visitStatus, dDay, reservedAt
) {
}
