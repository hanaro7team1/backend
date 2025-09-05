package com.sido.backend.realestate.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@NoArgsConstructor
public class RealEstateResponseDTO {
	private Long id;
	private String location;
	private String price;
	private String tradeType;
	private String imageUrl;
}
