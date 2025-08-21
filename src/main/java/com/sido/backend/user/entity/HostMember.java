package com.sido.backend.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class HostMember {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable=false, length = 30)
	private String villageName;

	@Column(nullable=false, length = 50, unique = true)
	private String loginId;

	private String password;

	@Column(nullable=false,length = 15, unique = true)
	private String phone;

	@Column(nullable=false)
	private String region;
}
