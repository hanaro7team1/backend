package com.sido.backend.festival.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class FestivalRequestDTO extends FestivalDTO {
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
