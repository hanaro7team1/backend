package com.sido.backend.house.entity;

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
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class House extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(length = 64, nullable = false)
	private String title;

	@Column(length = 64, nullable = false)
	private String address;

	@Column(nullable = false)
	private Integer capacity;

	@Column(nullable = false)
	private Integer areaSize;

	@Column(length = 512)
	private String description;

	@Column(nullable = false)
	@Builder.Default
	private boolean isHomestay = true;

	@Column(length = 31)
	private String ownerPhone;

	@ManyToOne
	@JoinColumn(name = "host", foreignKey = @ForeignKey(
		name = "fk_House_Member_host",
		foreignKeyDefinition = """
			foreign key (host) references Member(id)
			on delete set null
			on update cascade
			"""
	))
	private HostMember host;
}
