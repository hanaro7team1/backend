package com.sido.backend.reservation.service;

import com.sido.backend.reservation.dto.ReservationDTO;
import com.sido.backend.reservation.dto.ReservationResponseDTO;

public interface ReservationService {
	ReservationResponseDTO createReservation(long stayId, Long memberId, ReservationDTO reservationDTO);

	ReservationResponseDTO confirmReservation(long stayId, Long memberId);
}
