package com.sido.backend.realestate.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sido.backend.realestate.entity.RealEstateImage;

public interface RealEstateImageRepository extends JpaRepository<RealEstateImage, Long> {
}
