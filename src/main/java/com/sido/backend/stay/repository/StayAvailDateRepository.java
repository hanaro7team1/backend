package com.sido.backend.stay.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sido.backend.stay.entity.StayAvailDate;

public interface StayAvailDateRepository extends JpaRepository<StayAvailDate, Long> {
	// 특정 stay의 [from, toExclusive) 구간 내 예약 가능한 날짜 리스트
	@Query("""
			select sa.availableDate from StayAvailDate sa
				where sa.stay.id = :stayId
					and sa.isAvailable = true
					and sa.availableDate >= :from
					and sa.availableDate < :toExclusive
				order by sa.availableDate asc
		""")
	List<LocalDate> findAvailableDatesInRange(@Param("stayId") Long stayId, @Param("from") LocalDate start,
		@Param("toExclusive") LocalDate endExclusive);

	// start보다 이른 날짜에 예약 가능한 날이 있는지
	boolean existsByStayIdAndAvailableDateLessThanAndIsAvailableTrue(Long stayId, LocalDate start);

	// end보다 늦은 날짜에 예약 가능한 날이 있는지
	boolean existsByStayIdAndAvailableDateGreaterThanAndIsAvailableTrue(Long stayId, LocalDate end);

	// start, end 사이에 예약 가능한 날짜 개수
	long countByStayIdAndAvailableDateBetweenAndIsAvailableTrue(Long stayId, LocalDate start, LocalDate end);
}
