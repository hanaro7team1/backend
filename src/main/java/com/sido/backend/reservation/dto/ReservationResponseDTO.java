package com.sido.backend.reservation.dto;

import java.time.LocalDateTime;

import com.sido.backend.reservation.entity.ResrvStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ReservationResponseDTO extends ReservationDTO {
	private Long reservationId;
	private Long stayId;
	private ResrvStatus resrvStatus;
	private LocalDateTime reservedAt; // 예약 확정 시각
}
