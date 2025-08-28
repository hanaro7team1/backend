package com.sido.backend.realEstates.entity;

import java.util.List;

import com.sido.backend.common.entity.BaseEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RealEstates extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String address;  // 주소
	private Integer price;  // 가격
	private Integer capacity;  // 수용인원
	private Integer area;  // 면적
	private String description;  // 설명
	private Double areaSize;  // 세부 면적
	private String tradeType;  // 거래 유형 (전세, 매매)
	private Integer roomCount;  // 방 개수
	private String house;  // 구조 설명

	@OneToMany(mappedBy = "realEstate", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	private List<RealEstateImage> images;
}
