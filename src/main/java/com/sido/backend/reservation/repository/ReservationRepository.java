package com.sido.backend.reservation.repository;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sido.backend.reservation.entity.Reservation;
import com.sido.backend.reservation.entity.VisitStatus;

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

	// 호스트 탈퇴 전 예약 확인용
	boolean existsByStay_Host_IdAndVisitStatusIn(Long hostId, Collection<VisitStatus> statuses);

	interface ReservationCounts {
		long getUpcomingCnt();

		long getInProgressCnt();

		long getCompletedCnt();
	}
}
