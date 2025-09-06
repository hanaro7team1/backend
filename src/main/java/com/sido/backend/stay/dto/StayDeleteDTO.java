package com.sido.backend.stay.dto;

public record StayDeleteDTO(
	boolean deleted, // isActive -> false
	boolean hasActiveReservations // 방문 전, 방문 중 예약이 있는지
) {
}
