package com.sido.backend.stay.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record StaySpecDTO(
	@NotNull @Schema(name = "capacity", example = "8") Integer capacity,
	@NotNull @Schema(name = "areaSize", example = "86") Integer areaSize,
	@NotBlank @Size(min = 1, max = 1000) String description) {
}