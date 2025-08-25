package com.sido.backend.festival.entity;

import java.time.LocalDate;

import org.hibernate.annotations.DynamicInsert;

import com.sido.backend.common.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@DynamicInsert
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Festival extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(length = 64, nullable = false)
	private String title;

	@Column(nullable = false)
	private LocalDate startDate;

	@Column(nullable = false)
	private LocalDate endDate;

	@NotBlank
	@Size(min = 1, max = 31)
	private String city;

	@NotBlank
	@Size(min = 1, max = 31)
	private String street;

	@Column(nullable = false)
	private int price;

	@Column(length = 125, nullable = false)
	private String url;

	@Column(length = 512, nullable = false)
	private String description;
}
