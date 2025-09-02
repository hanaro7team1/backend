package com.sido.backend.reservation.dto;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.sido.backend.reservation.dto.ReservationCommonDTOs.ReservationInfoDTO;
import com.sido.backend.reservation.dto.ReservationCommonDTOs.StaySummaryDTO;
import com.sido.backend.reservation.entity.ResrvStatus;

public record ReservationDetailResponseDTO(
	Long reservationId,

	ResrvStatus resrvStatus,

	String memberName,

	String memberPhone,

	Boolean isHomestay,

	String ownerName,

	String ownerPhone,

	@JsonUnwrapped
	StaySummaryDTO staySummary, // stayId, title, address(상세주소 포함)

	@JsonUnwrapped
	ReservationInfoDTO reservationInfo // startDate, endDate, personCnt, isFarm

) {
}
