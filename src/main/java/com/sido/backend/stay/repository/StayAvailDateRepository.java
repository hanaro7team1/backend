package com.sido.backend.stay.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sido.backend.stay.entity.StayAvailDate;

public interface StayAvailDateRepository extends JpaRepository<StayAvailDate, Long> {
	/**
	 * 오픈
	 */
	// 오픈일 전체 조회
	@Query("select sa.availableDate from StayAvailDate sa where sa.id = :stayId")
	List<LocalDate> findAllDatesByStayId(@Param("stayId") Long stayId);

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

	// before (포함) 이전에 오픈한 날짜 목록 조회
	@Query("""
		select sa.availableDate from StayAvailDate sa
			where sa.stay.id = :stayId
				and sa.availableDate <= :before
			order by sa.availableDate asc
		""")
	List<LocalDate> findOpenOnBefore(@Param("stayId") Long stayId, @Param("before") LocalDate before);

	// [start, end) 기간 내 오픈한 날짜 있는지
	boolean existsByStayIdAndAvailableDateGreaterThanEqualAndAvailableDateLessThan(Long stayId, LocalDate start,
		LocalDate endExclusive);

	// end보다 늦은 날짜에 오픈된 날이 있는지 (end 포함)
	boolean existsByStayIdAndAvailableDateGreaterThanEqual(Long stayId, LocalDate end);

	/**
	 * 오픈 + 예약 미점유
	 */
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

	// end 이후 '오픈 + 예약 미점유' 날짜 개수
	@Query("""
		select count(sa) from StayAvailDate sa
			where sa.stay.id = :stayId
				and sa.availableDate >= :end
				and not exists (
					select 1 from ReservationDay rd
						where rd.stay.id = sa.stay.id
							and rd.date = sa.availableDate
					)
		""")
	long countOpenAndUnreservedOnOrAfter(@Param("stayId") Long stayId, @Param("end") LocalDate end);

	/**
	 * 오픈 + 예약 점유
	 */
	// [start, end) 기간 내 '오픈 + 예약 점유' 날짜 목록 조회
	@Query("""
		select sa.availableDate from StayAvailDate sa
			where sa.stay.id = :stayId
				and sa.availableDate >= :start
				and sa.availableDate < :endExclusive
				and exists (
					select 1 from ReservationDay rd
						where rd.stay.id = sa.stay.id
							and rd.date = sa.availableDate
					)
			order by sa.availableDate asc
		""")
	List<LocalDate> findOpenAndReservedInRange(@Param("stayId") Long stayId, LocalDate start, LocalDate endExclusive);

	// [start, end) 기간 내 '오픈 + 예약 점유' 날짜 개수
	@Query("""
		select count(sa) from StayAvailDate sa
			where sa.stay.id = :stayId
				and sa.availableDate >= :start
				and sa.availableDate < :endExclusive
				and exists (
					select 1 from ReservationDay rd
						where rd.stay.id = sa.stay.id
							and rd.date = sa.availableDate
					)
		""")
	long countOpenAndReservedInRange(@Param("stayId") Long stayId, @Param("start") LocalDate start,
		@Param("endExclusive") LocalDate endExclusive);

	// start(미포함) 이전 '오픈 + 예약 점유' 날짜 개수
	@Query("""
		select count(sa) from StayAvailDate sa
			where sa.stay.id = :stayId
				and sa.availableDate < :start
				and exists (
					select 1 from ReservationDay rd
						where rd.stay.id = sa.stay.id
							and rd.date = sa.availableDate
					)
		""")
	long countOpenAndReservedBefore(@Param("stayId") Long stayId, @Param("start") LocalDate start);

	// end 이후 '오픈 + 예약 점유' 날짜 개수
	@Query("""
		select count(sa) from StayAvailDate sa
			where sa.stay.id = :stayId
				and sa.availableDate >= :end
				and exists (
					select 1 from ReservationDay rd
						where rd.stay.id = sa.stay.id
							and rd.date = sa.availableDate
					)
		""")
	long countOpenAndReservedOnOrAfter(@Param("stayId") Long stayId, @Param("end") LocalDate end);

	@Modifying
	@Query("delete from StayAvailDate sa where sa.stay.id = :stayId")
	int deleteAllByStayId(@Param("stayId") Long stayId);
}
