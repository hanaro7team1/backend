package com.sido.backend.festival.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class FestivalDTO {
	private Long id;

	@NotBlank
	@Size(min = 1, max = 64)
	private String title;

	@NotNull
	private LocalDate startDate;

	@NotNull
	private LocalDate endDate;

	@NotBlank
	@Size(min = 1, max = 31)
	private String city;
}
