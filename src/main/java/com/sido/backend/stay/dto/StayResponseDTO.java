package com.sido.backend.stay.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StayResponseDTO {
	private Long id;
	private String title;

	private String address;
	private Boolean isHomestay;

	private StayResrvStatus stayResrvStatus;

	private String imageURL;

	private String hostName;
}
