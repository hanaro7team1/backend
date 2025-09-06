package com.sido.backend.stay.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RegionResponseDTO {
	private String region;
	private List<String> detailRegions;
}