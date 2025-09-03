package com.sido.backend.stay.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sido.backend.stay.entity.StayImage;

@Repository
public interface StayImageRepository extends JpaRepository<StayImage, Long> {
}