package com.sido.backend.festival.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ServiceRequestDTO {
	private Long id;

	@NotBlank
	@Size(min = 1, max = 64)
	private String title;

	@NotBlank
	private LocalDate startDate;

	@NotBlank
	private LocalDate endDate;

	@NotBlank
	@Size(min = 1, max = 31)
	private String city;

	@NotBlank
	@Size(min = 1, max = 31)
	private String street;

	@NotBlank
	@Size(min = 1, max = 16)
	private int price;

	@NotBlank
	@Size(min = 1, max = 125)
	private String url;

	@NotBlank
	@Size(min = 1, max = 512)
	private String description;
}
