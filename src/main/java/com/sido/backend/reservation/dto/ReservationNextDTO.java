package com.sido.backend.reservation.dto;

import com.fasterxml.jackson.annotation.JsonUnwrapped;

public record ReservationNextDTO(
	String memberName,

	@JsonUnwrapped
	ReservationListItemDTO reservationListItemDTO
) {
}
