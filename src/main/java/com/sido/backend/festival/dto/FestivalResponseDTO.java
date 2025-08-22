package com.sido.backend.festival.dto;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class FestivalResponseDTO {
	private Long id;
	private String title;
	private LocalDate startDate;
	private LocalDate endDate;
	private String city;
}
