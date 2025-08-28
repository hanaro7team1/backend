package com.sido.backend.realEstates.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@NoArgsConstructor
public class RealEstateDTO {
    private Long id;
    private String address;
    private Integer price;
    private Integer capacity;
    private Integer area;
    private String tradeType;
}
