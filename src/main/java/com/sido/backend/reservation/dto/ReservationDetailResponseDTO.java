package com.sido.backend.reservation.dto;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import com.fasterxml.jackson.annotation.JsonUnwrapped;

public record ReservationDetailResponseDTO(
	Long reservationId,

	String memberName,

	String memberPhone,

	String ownerName,

	String ownerPhone,

	@JsonUnwrapped
	@JsonIncludeProperties({"title", "address"})
	ReservationCommonDTOs.StaySummaryDTO staySummary, // address: 상세주소 포함

	@JsonUnwrapped
	ReservationCommonDTOs.ReservationInfoDTO reservationInfo // startDate, endDate, personCnt, isFarm

) {
}
