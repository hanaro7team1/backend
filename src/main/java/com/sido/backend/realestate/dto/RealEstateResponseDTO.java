package com.sido.backend.realestate.dto;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@NoArgsConstructor
public class RealEstateResponseDTO {
	private Long id;
	private String address;
	private Integer price;
	private String tradeType;
	private List<String> imageUrls;
}
