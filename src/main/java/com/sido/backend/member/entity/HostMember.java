package com.sido.backend.member.entity;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("HOST")
public class HostMember extends Member {
	@Column(nullable = false, length = 30) // nullable=false -> 서비스에서 구현해줘야 함.
	private String villageName;

	@Column(nullable = false) // nullable=false -> 서비스에서 구현해줘야 함.
	private String region;
}
