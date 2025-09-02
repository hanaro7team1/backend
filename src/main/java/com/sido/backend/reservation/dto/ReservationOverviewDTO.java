package com.sido.backend.reservation.dto;

import lombok.Builder;

@Builder
public record ReservationOverviewDTO(
	Long upcomingCnt,
	Long inProgressCnt,
	Long completedCnt
) {
}
