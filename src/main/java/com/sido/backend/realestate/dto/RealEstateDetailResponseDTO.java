package com.sido.backend.realestate.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@NoArgsConstructor
public class RealEstateDetailResponseDTO extends RealEstateResponseDTO {
	private Integer capacity;
	private Integer area;
	private String description;
	private Double areaSize;
	private Integer roomCount;
	private String house;
}
