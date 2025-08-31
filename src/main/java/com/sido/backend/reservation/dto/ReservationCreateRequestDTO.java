package com.sido.backend.reservation.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.Min;

public record ReservationCreateRequestDTO(
	LocalDate startDate,

	LocalDate endDate,

	@Min(1)
	Integer personCnt
) {
}
