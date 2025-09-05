package com.sido.backend.stay.repository;

import java.time.LocalDate;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sido.backend.stay.dto.StayResrvStatus;
import com.sido.backend.stay.entity.Stay;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public interface StayRepository extends JpaRepository<Stay, Long> {
	@Query("""
		SELECT s,
		       CASE
		           WHEN NOT EXISTS (
		               select 1 from StayAvailDate sa
		               where sa.stay.id = s.id
		                  and (:startDate IS NULL OR sa.availableDate >= :startDate)
		                  and (:endDate IS NULL OR sa.availableDate < :endDate)
		           ) THEN com.sido.backend.stay.dto.StayResrvStatus.SOLD_OUT
		           WHEN EXISTS (
		               select 1 from ReservationDay rd
		               where rd.stay.id = s.id
		                  and (:startDate IS NULL OR rd.date >= :startDate)
		                  and (:endDate IS NULL OR rd.date < :endDate)
		           ) THEN com.sido.backend.stay.dto.StayResrvStatus.CLOSED
		           ELSE com.sido.backend.stay.dto.StayResrvStatus.AVAILABLE
		       END as status
		FROM Stay s
		WHERE s.isActive = true
		AND (s.isHomestay = :isHomestay)
		AND (:address IS NULL OR s.address LIKE %:address%)
		AND (:capacity IS NULL OR s.capacity >= :capacity)
		"""
	)
	Slice<Object[]> findStaysDynamically(
		@Param("isHomestay") Boolean isHomestay,
		@Param("address") String address,
		@Param("startDate") LocalDate startDate,
		@Param("endDate") LocalDate endDate,
		@Param("capacity") Integer capacity,
		Pageable pageable
	);

	@Query("""
		select s,
		       case
		           when (select count(sa) from StayAvailDate sa
		                 where sa.stay.id = s.id and sa.availableDate >= CURRENT_DATE) = 0
		               then com.sido.backend.stay.dto.StayResrvStatus.CLOSED
		           when (select count(sa) from StayAvailDate sa
		                 where sa.stay.id = s.id and sa.availableDate >= CURRENT_DATE)
		                = (select count(rd) + 1 from ReservationDay rd
		                   where rd.stay.id = s.id and rd.date >= CURRENT_DATE)
		               then com.sido.backend.stay.dto.StayResrvStatus.SOLD_OUT
		           else com.sido.backend.stay.dto.StayResrvStatus.AVAILABLE
		       end as status
		from Stay s
		where s.host.id = :memberId and s.isHomestay = true and s.isActive = true
		"""
	)
	Slice<Object[]> findByHostWithStatus(
		@Param("memberId") Long memberId, Pageable pageable);

	boolean existsByAddressAndDetailAddress(@NotBlank @Size(min = 1, max = 64) String address,
		@NotBlank @Size(min = 1, max = 64) String detailAddress);

	@Query("""
			SELECT
				CASE
					WHEN (
						SELECT COUNT(sa) FROM StayAvailDate sa
							WHERE sa.stay.id = s.id
					) = 0
						THEN com.sido.backend.stay.dto.StayResrvStatus.CLOSED
					WHEN (
						SELECT COUNT(rd) FROM ReservationDay rd
							WHERE rd.stay.id = s.id
					) = (
						SELECT COUNT(sa) FROM StayAvailDate sa
							WHERE sa.stay.id = s.id
					)
						THEN com.sido.backend.stay.dto.StayResrvStatus.SOLD_OUT
					ELSE com.sido.backend.stay.dto.StayResrvStatus.AVAILABLE
				END AS status
			FROM Stay s
			WHERE s.id = :stayId
		""")
	StayResrvStatus findResrvStatusByStayId(@Param("stayId") Long stayId);
}
