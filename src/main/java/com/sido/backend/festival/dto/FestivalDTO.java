package com.sido.backend.festival.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class FestivalDTO {
	private Long id;

	@NotNull
	private String title;

	@NotNull
	private LocalDate startDate;

	@NotNull
	private LocalDate endDate;

	@NotNull
	private String region;

	@NotNull
	private int price;

	@NotNull
	private String url;

	@NotNull
	private String description;

	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
}
