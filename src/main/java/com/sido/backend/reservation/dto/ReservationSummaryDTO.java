package com.sido.backend.reservation.dto;

import lombok.Builder;

@Builder
public record ReservationSummaryDTO(
	Long upcomingCnt,
	Long inProgressCnt,
	Long completedCnt
) {
}
