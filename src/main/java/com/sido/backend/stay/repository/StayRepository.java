package com.sido.backend.stay.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sido.backend.stay.entity.Stay;

public interface StayRepository extends JpaRepository<Stay, Long> {
}
