package com.sido.backend.member.entity;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@DiscriminatorValue("HOST")
public class HostMember extends Member {
	@Column(nullable = false, length = 30) // DTO or 서비스에서 nullable 검증 필요
	private String villageName;

	@Column(nullable = false) // DTO or 서비스에서 nullable 검증 필요
	private String region;
}
