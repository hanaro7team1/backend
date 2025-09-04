package com.sido.backend.reservation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservationNotificationDTO {
	private Long reservationId;
	private String roomName; // 사랑방 이름
	private String confirmedDate; // 확정한 예약 일정
	private String roomOwnerName; // 사랑방 주인 이름
}
