package com.sido.backend.stay.entity;

import com.sido.backend.common.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class StayImage extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private String id;

	@Column(nullable = false)
	private String orgName;

	@Column(nullable = false)
	private String saveName;

	@Column(nullable = false)
	private String saveUrl;

	@Column(nullable = false)
	private String s3Key;

	@ManyToOne
	@JoinColumn(name = "stayId", nullable = false)
	private Stay stay;
}