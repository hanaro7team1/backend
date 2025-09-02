package com.sido.backend.stay.entity;

import com.sido.backend.common.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class StayImage extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(length = 512, nullable = false, unique = true)
	private String s3Key;

	// 이미지 순서(1번은 대표)
	@Column(nullable = false)
	private int sortOrder = 0;

	@ManyToOne
	@JoinColumn(name = "stay", nullable = false)
	private Stay stay;
}