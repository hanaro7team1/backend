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
					SELECT 1 FROM StayAvailDate sa
						WHERE sa.stay.id = s.id
							AND (:startDate IS NULL OR sa.availableDate >= :startDate)
							AND (:endDate IS NULL OR sa.availableDate < :endDate)
				) THEN com.sido.backend.stay.dto.StayResrvStatus.CLOSED
				WHEN EXISTS (
					SELECT 1 FROM ReservationDay rd
						WHERE rd.stay.id = s.id
							AND (:startDate IS NULL OR rd.date >= :startDate)
							AND (:endDate IS NULL OR rd.date < :endDate)
				) THEN com.sido.backend.stay.dto.StayResrvStatus.SOLD_OUT
				ELSE com.sido.backend.stay.dto.StayResrvStatus.AVAILABLE
			END AS status
		FROM Stay s
			WHERE s.isActive = true
				AND (s.isHomestay = :isHomestay)
				AND (:address IS NULL OR s.address LIKE %:address%)
				AND (:capacity IS NULL OR s.capacity >= :capacity)
		ORDER BY
			CASE
				WHEN NOT EXISTS (
					SELECT 1 FROM StayAvailDate sa
						WHERE sa.stay.id = s.id
							AND (:startDate IS NULL OR sa.availableDate >= :startDate)
							AND (:endDate IS NULL OR sa.availableDate < :endDate)
				) THEN 3
				WHEN EXISTS (
					SELECT 1 FROM ReservationDay rd
						WHERE rd.stay.id = s.id
							AND (:startDate IS NULL OR rd.date >= :startDate)
							AND (:endDate IS NULL OR rd.date < :endDate)
				) THEN 2
				ELSE 1
			END ASC,
			s.id DESC
		""")
	Slice<Object[]> findStaysDynamically(
		@Param("isHomestay") Boolean isHomestay,
		@Param("address") String address,
		@Param("startDate") LocalDate startDate,
		@Param("endDate") LocalDate endDate,
		@Param("capacity") Integer capacity,
		Pageable pageable
	);

	@Query("""
		SELECT s,
			CASE
				WHEN (
					SELECT COUNT(sa) FROM StayAvailDate sa
						WHERE sa.stay.id = s.id
							AND sa.availableDate >= CURRENT_DATE
				) = 0
					THEN com.sido.backend.stay.dto.StayResrvStatus.CLOSED
				WHEN (
					SELECT COUNT(sa) FROM StayAvailDate sa
						WHERE sa.stay.id = s.id
							AND sa.availableDate >= CURRENT_DATE
				) = (
					SELECT COUNT(rd) FROM ReservationDay rd
						WHERE rd.stay.id = s.id
							AND rd.date >= CURRENT_DATE
				)
					THEN com.sido.backend.stay.dto.StayResrvStatus.SOLD_OUT
				ELSE com.sido.backend.stay.dto.StayResrvStatus.AVAILABLE
			END AS status
		FROM Stay s
			WHERE s.host.id = :memberId
				AND s.isHomestay = true
				AND s.isActive = true
		ORDER BY
			CASE
				WHEN NOT EXISTS (
					SELECT 1 FROM StayAvailDate sa
						WHERE sa.stay.id = s.id
							AND (:startDate IS NULL OR sa.availableDate >= :startDate)
							AND (:endDate IS NULL OR sa.availableDate < :endDate)
				) THEN 3
				WHEN EXISTS (
					SELECT 1 FROM ReservationDay rd
						WHERE rd.stay.id = s.id
							AND (:startDate IS NULL OR rd.date >= :startDate)
							AND (:endDate IS NULL OR rd.date < :endDate)
				) THEN 2
				ELSE 1
			END ASC,
			s.id DESC
		""")
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
							AND sa.availableDate >= CURRENT_DATE
				) = 0
					THEN com.sido.backend.stay.dto.StayResrvStatus.CLOSED
				WHEN (
					SELECT COUNT(sa) FROM StayAvailDate sa
						WHERE sa.stay.id = s.id
							AND sa.availableDate >= CURRENT_DATE
				) = (
					SELECT COUNT(rd) FROM ReservationDay rd
						WHERE rd.stay.id = s.id
							AND rd.date >= CURRENT_DATE
				)
					THEN com.sido.backend.stay.dto.StayResrvStatus.SOLD_OUT
				ELSE com.sido.backend.stay.dto.StayResrvStatus.AVAILABLE
			END AS status
		FROM Stay s
			WHERE s.id = :stayId
		""")
	StayResrvStatus findResrvStatusByStayIdForHost(@Param("stayId") Long stayId);

	@Query("""
		SELECT
			CASE
				WHEN NOT EXISTS (
					SELECT 1 FROM StayAvailDate sa
						WHERE sa.stay.id = s.id
							AND (:startDate IS NULL OR sa.availableDate >= :startDate)
							AND (:endDate IS NULL OR sa.availableDate < :endDate)
				) THEN com.sido.backend.stay.dto.StayResrvStatus.CLOSED
				WHEN EXISTS (
					SELECT 1 FROM ReservationDay rd
						WHERE rd.stay.id = s.id
							AND (:startDate IS NULL OR rd.date >= :startDate)
							AND (:endDate IS NULL OR rd.date < :endDate)
				) THEN com.sido.backend.stay.dto.StayResrvStatus.SOLD_OUT
				ELSE com.sido.backend.stay.dto.StayResrvStatus.AVAILABLE
			END AS status
		FROM Stay s
			WHERE s.id = :stayId
		""")
	StayResrvStatus findResrvStatusInRangeByStayId(@Param("stayId") Long stayId,
		@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}
