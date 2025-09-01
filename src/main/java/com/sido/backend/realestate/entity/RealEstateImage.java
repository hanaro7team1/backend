package com.sido.backend.realestate.entity;

import com.sido.backend.common.entity.BaseEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
public class RealEstateImage extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String orgname;  // 원본 이름
	private String savename;  // 저장 이름
	private String savedir;  // 저장 경로

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "realEstate", foreignKey = @ForeignKey(
		name = "fk_RealEstateImage_RealEstate",
		foreignKeyDefinition = "foreign key (realEstate) references realEstate(id) on delete cascade on update cascade"))
	private RealEstate realEstate;
}
