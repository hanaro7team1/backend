package com.sido.backend.reservation.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sido.backend.reservation.entity.ReservationDay;

public interface ReservationDayRepository extends JpaRepository<ReservationDay, Long> {
	void deleteByReservationId(Long reservationId);

	@Query("""
			SELECT rd.date FROM ReservationDay rd
				WHERE rd.stay.id = :stayId
					AND rd.date IN :dates
		""")
	List<LocalDate> findReservedDatesIn(@Param("stayId") Long stayId, @Param("dates") List<LocalDate> dates);

	@Query("""
			SELECT rd.date FROM ReservationDay rd
				WHERE rd.stay.id = :stayId
				ORDER BY rd.date ASC
		""")
	List<LocalDate> findAllReserved(Long stayId);
}
