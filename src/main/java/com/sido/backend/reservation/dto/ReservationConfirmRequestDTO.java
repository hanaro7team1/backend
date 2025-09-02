package com.sido.backend.reservation.dto;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.sido.backend.reservation.dto.ReservationCommonDTOs.ReservationInfoDTO;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record ReservationConfirmRequestDTO(
	@Valid
	@NotNull
	@JsonUnwrapped
	ReservationInfoDTO reservationInfo // startDate, endDate, personCnt, isFarm
) {
}
