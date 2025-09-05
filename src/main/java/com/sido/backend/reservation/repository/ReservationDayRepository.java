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
			select rd.date from ReservationDay rd
				where rd.stay.id = :stayId
					and rd.date in :dates
		""")
	List<LocalDate> findReservedDatesIn(@Param("stayId") Long stayId, @Param("dates") List<LocalDate> dates);
}
