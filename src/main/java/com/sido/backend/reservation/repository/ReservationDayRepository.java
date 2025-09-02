package com.sido.backend.reservation.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sido.backend.reservation.entity.ReservationDay;

public interface ReservationDayRepository extends JpaRepository<ReservationDay, Long> {
	void deleteByReservationId(Long reservationId);
}
