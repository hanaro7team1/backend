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
public class StayRegisterDTO {
	private boolean isHomestay;

	@NotBlank
	@Size(min = 1, max = 64)
	private String title;

	@NotBlank
	@Size(min = 1, max = 64)
	private String address;

	@NotNull
	private short capacity;

	@NotNull
	private short areaSize;

	@NotBlank
	@Size(min = 1, max = 9)
	private String owner;

	@NotBlank
	@Size(min = 1, max = 31)
	private String ownerPhone;

	@NotBlank
	@Size(min = 1, max = 512)
	private String description;
}
