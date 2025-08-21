package com.sido.backend.house.entity;

import com.sido.backend.common.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class House extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(length = 64, nullable = false)
	private String title;

	@Column(length = 64, nullable = false)
	private String address;

	@Column(nullable = false)
	private Short capacity;

	@Column(nullable = false)
	private Short areaSize;

	@Column(length = 512, nullable = true)
	private String description;

	@Column(nullable = false)
	private Boolean isHomestay;

	@Column(length = 31, nullable = true)
	private String ownerPhone;
}
