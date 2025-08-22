package com.sido.backend.festival.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class FestivalResponseDetailDTO extends FestivalResponseDTO {
	private String price;
	private String url;
	private String description;

	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
}
