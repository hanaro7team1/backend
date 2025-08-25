package com.sido.backend.house.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sido.backend.house.entity.House;

public interface HouseRepository extends JpaRepository<House, Long> {
}
