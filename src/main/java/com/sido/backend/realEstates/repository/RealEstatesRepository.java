package com.sido.backend.realEstates.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.sido.backend.realEstates.entity.RealEstates;

public interface RealEstatesRepository extends JpaRepository<RealEstates, Long> {
	@EntityGraph(attributePaths = {"images"})
	Optional<RealEstates> findById(Long id);
}
