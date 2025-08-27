package com.sido.backend.stay.dto;

import java.time.LocalDateTime;
import java.util.List;


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
