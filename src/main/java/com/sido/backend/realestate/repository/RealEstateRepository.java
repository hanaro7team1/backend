package com.sido.backend.realestate.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.sido.backend.realestate.entity.RealEstate;

public interface RealEstateRepository extends JpaRepository<RealEstate, Long> {
	@EntityGraph(attributePaths = {"images"})
	Optional<RealEstate> findById(Long id);
}
