package com.sido.backend.reservation.dto;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import com.fasterxml.jackson.annotation.JsonUnwrapped;

public record ReservationListResponseDTO(
	Long reservationId,

	String title,

	@JsonUnwrapped @JsonIncludeProperties({"resrvStatus", "visitStatus", "dDay"})
	ReservationCommonDTOs.ReservationStatusDTO status,

	@JsonUnwrapped @JsonIncludeProperties({"startDate", "endDate"})
	ReservationCommonDTOs.ReservationInfoDTO reservationInfo
) {
}
