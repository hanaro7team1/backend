package com.sido.backend.reservation.service;

import com.sido.backend.common.dto.PageResponseDTO;
import com.sido.backend.reservation.dto.ReservationConfirmRequestDTO;
import com.sido.backend.reservation.dto.ReservationConfirmResponseDTO;
import com.sido.backend.reservation.dto.ReservationCreateRequestDTO;
import com.sido.backend.reservation.dto.ReservationCreateResponseDTO;
import com.sido.backend.reservation.dto.ReservationDetailResponseDTO;
import com.sido.backend.reservation.dto.ReservationListFilter;
import com.sido.backend.reservation.dto.ReservationListItemDTO;
import com.sido.backend.reservation.dto.ReservationOverviewDTO;
import com.sido.backend.reservation.entity.Reservation;

public interface ReservationService {
	ReservationCreateResponseDTO createReservation(Long memberId, Long stayId,
		ReservationCreateRequestDTO createRequest);

	ReservationConfirmResponseDTO confirmReservation(Long memberId, Long reservationId,
		ReservationConfirmRequestDTO confirmRequest);

	ReservationDetailResponseDTO getReservationDetail(Long memberId, Long reservationId);

	void cancelReservation(Long memberId, Long reservationId);

	ReservationOverviewDTO getReservationOverview(Long memberId);

	PageResponseDTO<ReservationListItemDTO, Reservation> getReservationList(Long memberId, int page, int listSize,
		ReservationListFilter filter);

	ReservationListItemDTO getNextReservation(Long memberId);

	PageResponseDTO<ReservationListItemDTO, Reservation> getAdminReservationList(Long memberId, int page, int listSize,
		ReservationListFilter filter);
}
