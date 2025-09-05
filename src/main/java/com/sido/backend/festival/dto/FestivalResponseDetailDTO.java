package com.sido.backend.festival.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class FestivalResponseDetailDTO extends FestivalDTO {
	private String location;
	private int price;
	private String url;
	private String description;
}
