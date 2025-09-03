package com.sido.backend.reservation.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import com.sido.backend.reservation.dto.ReservationListFilter;
import com.sido.backend.reservation.entity.Reservation;

public interface ReservationQDslRepository {
	Slice<Reservation> findList(Long memberId, ReservationListFilter filter, Pageable pageable);

	Slice<Reservation> findAdminList(Long memberId, ReservationListFilter filter, Pageable pageable);
}
