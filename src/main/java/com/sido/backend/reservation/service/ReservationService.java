package com.sido.backend.reservation.service;

import com.sido.backend.reservation.dto.ReservationConfirmRequestDTO;
import com.sido.backend.reservation.dto.ReservationConfirmResponseDTO;
import com.sido.backend.reservation.dto.ReservationCreateRequestDTO;
import com.sido.backend.reservation.dto.ReservationCreateResponseDTO;
import com.sido.backend.reservation.dto.ReservationDetailResponseDTO;
import com.sido.backend.reservation.dto.ReservationOverviewDTO;

public interface ReservationService {
	ReservationCreateResponseDTO createReservation(Long memberId, Long stayId,
		ReservationCreateRequestDTO createRequest);

	ReservationConfirmResponseDTO confirmReservation(Long memberId, Long reservationId,
		ReservationConfirmRequestDTO confirmRequest);

	ReservationDetailResponseDTO getReservationDetail(Long memberId, Long reservationId);

	void cancelReservation(Long memberId, Long reservationId);

	ReservationOverviewDTO getReservationOverview(Long memberId);
}
