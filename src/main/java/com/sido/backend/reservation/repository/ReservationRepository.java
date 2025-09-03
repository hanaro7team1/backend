package com.sido.backend.reservation.repository;

import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sido.backend.reservation.entity.Reservation;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
	@Query("""
		select
		    coalesce(sum(case when r.visitStatus = com.sido.backend.reservation.entity.VisitStatus.UPCOMING then 1 else 0 end), 0) as upcomingCnt,
		    coalesce(sum(case when r.visitStatus = com.sido.backend.reservation.entity.VisitStatus.IN_PROGRESS then 1 else 0 end), 0) as inProgressCnt,
			coalesce(sum(case when r.visitStatus = com.sido.backend.reservation.entity.VisitStatus.COMPLETED then 1 else 0 end), 0) as completedCnt
		from Reservation r
			where r.stay.host.id = :hostId
		""")
	ReservationCounts summarizeByHost(@Param("hostId") Long hostId);

	@Query("""
			select r from Reservation r
				where (r.visitStatus = com.sido.backend.reservation.entity.VisitStatus.UPCOMING
						or r.visitStatus = com.sido.backend.reservation.entity.VisitStatus.IN_PROGRESS)
						and r.member.id = :memberId
				order by r.startDate asc
						limit 1
		""")
	Optional<Reservation> findNextReservation(@Param("memberId") Long memberId);

	Slice<Reservation> findByMemberId(Long memberId, PageRequest of);

	// @Query("""
	// 		select r from Reservation r
	// 			where r.member.id = :memberId
	//
	// 	""")
	interface ReservationCounts {
		long getUpcomingCnt();

		long getInProgressCnt();

		long getCompletedCnt();
	}
}
