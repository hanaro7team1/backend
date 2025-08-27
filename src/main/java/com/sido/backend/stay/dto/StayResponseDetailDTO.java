package com.sido.backend.stay.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class StayResponseDetailDTO {
	private Long id;
	private String title;

	private String address;
	private Integer capacity;
	private Integer areaSize;
	private String description;
	private Boolean isHomestay;

	private String isActiveMsg;
}
