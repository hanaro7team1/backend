package com.sido.backend.realestate.repository;

import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sido.backend.realestate.entity.RealEstate;

public interface RealEstateRepository extends JpaRepository<RealEstate, Long> {
	@EntityGraph(attributePaths = {"images"})
	Optional<RealEstate> findById(Long id);

	@EntityGraph(attributePaths = {"images"})
	@Query("SELECT re FROM RealEstate re " +
		"WHERE (:location IS NULL OR :location = '''' OR re.location LIKE CONCAT('%', :location, '%')) " +
		"AND (:tradeType IS NULL OR :tradeType = '''' OR re.tradeType = :tradeType) " +
		"AND (:minPrice IS NULL OR re.price >= :minPrice) " +
		"AND (:maxPrice IS NULL OR re.price <= :maxPrice)")
	Slice<RealEstate> findRealEstatesDynamically(
		@Param("location") String location,
		@Param("tradeType") String tradeType,
		@Param("minPrice") Integer minPrice,
		@Param("maxPrice") Integer maxPrice,
		Pageable pageable
	);
}
