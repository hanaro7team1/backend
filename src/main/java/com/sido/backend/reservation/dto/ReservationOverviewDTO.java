package com.sido.backend.reservation.dto;

import lombok.Builder;

@Builder
public record ReservationOverviewDTO(
	String villageName,
	Long upcomingCnt,
	Long inProgressCnt,
	Long completedCnt
) {
}
