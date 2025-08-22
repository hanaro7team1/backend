package com.sido.backend.festival.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ServiceRequestDTO {
	private long id;

	@NotBlank
	@Size(min = 1, max = 31)
	private String date;

	@NotBlank
	@Size(min = 1, max = 64)
	private String region;

	@NotBlank
	@Size(min = 1, max = 64)
	private String title;

	@NotBlank
	@Size(min = 1, max = 125)
	private String url;

	@NotBlank
	@Size(min = 1, max = 512)
	private String description;
}
