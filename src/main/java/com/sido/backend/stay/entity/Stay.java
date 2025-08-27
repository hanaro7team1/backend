package com.sido.backend.stay.entity;

import org.hibernate.annotations.DynamicInsert;

import com.sido.backend.common.entity.BaseEntity;
import com.sido.backend.member.entity.HostMember;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@DynamicInsert
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Stay extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	//title 생성되는 id 기반으로 만듦
	@Column(length = 64, nullable = false)
	private String title;

	@Column(length = 64, nullable = false)
	private String address;

	@Column(length = 64, nullable = false)
	private String detailAddress;

	@Column(nullable = false)
	private Integer capacity;

	@Column(nullable = false)
	private Integer areaSize;

	@Column(length = 1000, nullable = false)
	private String description;

	@Column(nullable = false)
	@Builder.Default
	private Boolean isHomestay = true;

	@Column(length = 9) // 독립형: 집주인 X -> nullable
	private String ownerName;

	@Column(length = 31) // 독립형: 집주인 X -> nullable
	private String ownerPhone;

	@Column(nullable = false)
	@Builder.Default
	private Boolean isActive = true; // stay 삭제 처리 시 false로. 조회할 땐 true인 것들만.

	@ManyToOne
	@JoinColumn(name = "host", foreignKey = @ForeignKey(
		name = "fk_Stay_Member_host",
		foreignKeyDefinition = """
			foreign key (host) references Member(id)
			on delete set null
			on update cascade
			"""
	))
	private HostMember host;
}