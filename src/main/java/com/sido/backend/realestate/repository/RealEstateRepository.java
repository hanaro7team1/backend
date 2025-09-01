package com.sido.backend.realestate.repository;

import com.sido.backend.realestate.entity.RealEstate;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface RealEstateRepository extends JpaRepository<RealEstate, Long> {
    @EntityGraph(attributePaths = {"images"})
    Optional<RealEstate> findById(Long id);

    @Query("SELECT re FROM RealEstate re " +
            "WHERE (COALESCE(:address, '') = '' OR re.address LIKE %:address%) " +
            "AND (COALESCE(:tradeType, '') = '' OR re.tradeType = :tradeType) " +
            "AND (:minPrice IS NULL OR re.price >= :minPrice) " +
            "AND (:maxPrice IS NULL OR re.price <= :maxPrice)")
    Slice<RealEstate> findRealEstatesDynamically(
            @Param("address") String address,
            @Param("tradeType") String tradeType,
            @Param("minPrice") Integer minPrice,
            @Param("maxPrice") Integer maxPrice,
            Pageable pageable
    );
}
