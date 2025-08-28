package com.sido.backend.stay.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sido.backend.stay.entity.Stay;
import com.sido.backend.stay.entity.StayAvailDate;

public interface StayAvailDateRepository extends JpaRepository<StayAvailDate, Long> {
	// [start, end) 기간 내 오픈한 날짜 목록 조회
	@Query("""
		select sa.availableDate from StayAvailDate sa
			where sa.stay.id = :stayId
				and sa.availableDate >= :start
				and sa.availableDate < :endExclusive
			order by sa.availableDate asc
		""")
	List<LocalDate> findOpenInRange(@Param("stayId") Long stayId, @Param("start") LocalDate start,
		@Param("endExclusive") LocalDate endExclusive);

	// [start, end) 기간 내 오픈한 날짜 있는지
	boolean existsByStayIdAndAvailableDateGreaterThanEqualAndAvailableDateLessThan(Long stayId, LocalDate start,
		LocalDate endExclusive);

	// end보다 늦은 날짜에 오픈된 날이 있는지 (end 포함)
	boolean existsByStayIdAndAvailableDateGreaterThanEqual(Long stayId, LocalDate end);

	// [start, end) 기간 내 '오픈 + 예약 미점유' 날짜 목록 조회
	@Query("""
		select sa.availableDate from StayAvailDate sa
			where sa.stay.id = :stayId
				and sa.availableDate >= :start
				and sa.availableDate < :endExclusive
				and not exists (
					select 1 from ReservationDay rd
						where rd.stay.id = sa.stay.id
							and rd.date = sa.availableDate
					)
			order by sa.availableDate asc
		""")
	List<LocalDate> findOpenAndUnreservedInRange(@Param("stayId") Long stayId, LocalDate start, LocalDate endExclusive);

	// [start, end) 기간 내 '오픈 + 예약 미점유' 날짜 개수
	@Query("""
		select count(sa) from StayAvailDate sa
			where sa.stay.id = :stayId
				and sa.availableDate >= :start
				and sa.availableDate < :endExclusive
				and not exists (
					select 1 from ReservationDay rd
						where rd.stay.id = sa.stay.id
							and rd.date = sa.availableDate
					)
		""")
	long countOpenAndUnreservedInRange(@Param("stayId") Long stayId, @Param("start") LocalDate start,
		@Param("endExclusive") LocalDate endExclusive);

	// from 이후 '오픈 + 예약 미점유' 날짜 개수
	@Query("""
		select count(sa) from StayAvailDate sa
			where sa.stay.id = :stayId
				and sa.availableDate >= :from
				and not exists (
					select 1 from ReservationDay rd
						where rd.stay.id = sa.stay.id
							and rd.date = sa.availableDate
					)
		""")
	long countOpenAndUnreservedOnOrAfter(@Param("stayId") Long stayId, @Param("from") LocalDate from);

	List<StayAvailDate> findByStayAndAvailableDateBetweenOrderByAvailableDateAsc(Stay stay, LocalDate start,
		LocalDate end);
}
