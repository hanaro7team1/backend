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
	@NotBlank
	@Size(min = 1, max = 64)
	private String title;

	@NotBlank
	@Size(min = 1, max = 64)
	private String address;

	@NotNull
	private Integer capacity;

	@NotNull
	private Integer areaSize;

	@NotBlank
	@Size(min = 1, max = 9)
	private String ownerName;

	@NotBlank
	@Size(min = 1, max = 31)
	private String ownerPhone;

	@NotBlank
	@Size(min = 1, max = 512)
	private String description;
}
