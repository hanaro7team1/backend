package com.sido.backend.stay.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class StayEditDTO {
	private Long id;

	@NotNull
	private short capacity;

	@NotNull
	private short areaSize;

	@NotBlank
	@Size(min = 1, max = 512)
	private String description;
}
