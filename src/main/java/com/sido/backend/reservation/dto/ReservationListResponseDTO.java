package com.sido.backend.reservation.dto;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.sido.backend.reservation.dto.ReservationCommonDTOs.ReservationInfoDTO;
import com.sido.backend.reservation.dto.ReservationCommonDTOs.ReservationStatusDTO;

public record ReservationListResponseDTO(
	Long reservationId,

	String title,

	@JsonUnwrapped @JsonIncludeProperties({"resrvStatus", "visitStatus", "dDay"})
	ReservationStatusDTO status,

	@JsonUnwrapped @JsonIncludeProperties({"startDate", "endDate"})
	ReservationInfoDTO reservationInfo
) {
}
