package com.sido.backend.member.entity;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "memberType", length = 10) // USER, ADMIN
@DiscriminatorValue("USER")
@EqualsAndHashCode(callSuper = false)
public class Member {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(length = 30)
	private String name; // 일반 회원: 이름

	@Column(nullable = false, length = 50, unique = true)
	private String loginId;

	@Column(nullable = false, length = 250)
	private String password; // 영문자 , 숫자, 특수문자 포함 8~20자

	@Column(nullable = false, length = 15, unique = true)
	private String phone; // 일반 회원: 개인 연락처, Host: 면사무소 연락처

	@Enumerated(EnumType.STRING)
	@Builder.Default
	private MemberRole role = MemberRole.ROLE_USER; // USER, ADMIN
}
