package com.sido.backend.reservation.dto;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.sido.backend.reservation.dto.ReservationCommonDTOs.ReservationInfoDTO;
import com.sido.backend.reservation.dto.ReservationCommonDTOs.StaySummaryDTO;
import com.sido.backend.reservation.entity.ResrvStatus;

public record ReservationCreateResponseDTO(
	Long reservationId,

	@JsonUnwrapped
	StaySummaryDTO staySummary, // stayId, title, address (상세 주소 X)

	@JsonUnwrapped
	@JsonIncludeProperties({"startDate", "endDate", "personCnt"})
	ReservationInfoDTO reservationInfo,

	ResrvStatus resrvStatus
) {
}
