package com.sido.backend.stay.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sido.backend.stay.entity.Stay;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public interface StayRepository extends JpaRepository<Stay, Long> {
	boolean existsByAddressAndDetailAddress(@NotBlank @Size(min = 1, max = 64) String address,
		@NotBlank @Size(min = 1, max = 64) String detailAddress);
}