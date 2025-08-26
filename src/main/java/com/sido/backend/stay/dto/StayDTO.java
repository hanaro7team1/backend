package com.sido.backend.stay.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class StayDTO {
	@NotNull
	@Schema(name = "capacity", example = "8")
	private Integer capacity;

	@NotNull
	@Schema(name = "areaSize", example = "86")
	private Integer areaSize;

	@NotBlank
	@Size(min = 1, max = 1000)
	private String description;
}
