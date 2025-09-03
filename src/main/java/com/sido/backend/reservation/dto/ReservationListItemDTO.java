package com.sido.backend.reservation.dto;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.sido.backend.reservation.dto.ReservationCommonDTOs.ReservationInfoDTO;

public record ReservationListItemDTO(
	Long reservationId,

	String imageURL,

	String title,

	ReservationViewStatus viewStatus, // UPCOMING(방문 전), IN_PROGRESS(방문 중), COMPLETED(방문 완료), CANCELLED(예약 취소)

	Long dDay,

	@JsonUnwrapped @JsonIncludeProperties({"startDate", "endDate"})
	ReservationInfoDTO reservationInfo
) {
}
