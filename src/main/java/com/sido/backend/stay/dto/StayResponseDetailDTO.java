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
    private List<String> imageUrls;

    private String address;
    private Integer capacity;
    private Integer areaSize;
    private String description;
    private Boolean isHomestay;
    private String ownerName;
    private String ownerPhone;
    private String hostVillageName;
    private String hostRegion;
    private String hostPhone;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
