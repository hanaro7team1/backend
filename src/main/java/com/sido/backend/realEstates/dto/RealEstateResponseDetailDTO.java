package com.sido.backend.realEstates.dto;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@NoArgsConstructor
public class RealEstateResponseDetailDTO extends RealEstateDTO {
	private List<String> imageUrls;
	private String description;
	private Double areaSize;
	private Integer roomCount;
	private String house;
}
