package com.sido.backend.reservation.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sido.backend.reservation.entity.Reservation;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
}
