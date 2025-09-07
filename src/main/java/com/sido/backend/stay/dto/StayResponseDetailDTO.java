package com.sido.backend.stay.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StayResponseDetailDTO {
	private Long id;
	private String title;

	private StayResrvStatus stayResrvStatus;

	private String address;
	private String detailAddress;
	private Integer capacity;
	private Integer areaSize;
	private String description;
	private Boolean isHomestay;
	private Boolean isDeleted;
	private List<String> images;
}
