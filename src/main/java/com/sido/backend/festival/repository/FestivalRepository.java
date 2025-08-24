package com.sido.backend.festival.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sido.backend.festival.entity.Festival;

public interface FestivalRepository extends JpaRepository<Festival, Long> {
}
