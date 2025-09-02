package com.sido.backend.reservation.dto;

import com.fasterxml.jackson.annotation.JsonUnwrapped;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record ReservationConfirmRequestDTO(
	@Valid
	@NotNull
	@JsonUnwrapped
	ReservationCommonDTOs.ReservationInfoDTO reservationInfo // startDate, endDate, personCnt, isFarm
) {
}
