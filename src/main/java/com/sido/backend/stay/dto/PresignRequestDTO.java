package com.sido.backend.stay.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PresignRequestDTO {
	@Schema(example = "temp")
	private String domain;

	@Schema(example = "jpg")
	private String extension;

	@Schema(example = "image/jpeg")
	private String contentType;
}