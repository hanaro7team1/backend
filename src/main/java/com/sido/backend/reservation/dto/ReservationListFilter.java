package com.sido.backend.reservation.dto;

public enum ReservationListFilter {
	ALL, // 전체
	RESERVED, // 예약 됨 viewStatus: UPCOMING(방문 전), IN_PROGRESS(방문 중) - visitStatus
	COMPLETED, // 방문 완료 viewStatus: COMPLETED(방문 완료) - visitStatus
	CANCELLED, // 취소 됨 viewStatus: CANCELLED(예약 취소) - resrvStatus
}
