package com.sido.backend.reservation.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.sido.backend.common.entity.BaseEntity;
import com.sido.backend.member.entity.Member;
import com.sido.backend.stay.entity.Stay;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Reservation extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private LocalDate startDate;

	@Column(nullable = false)
	private LocalDate endDate;

	@Column(nullable = false)
	private Integer personCnt;

	@Column(nullable = false)
	private Boolean isFarm;

	@Enumerated(EnumType.STRING)
	private ResrvStatus resrvStatus;

	@Column
	private LocalDateTime reservedAt; // resrvStatus가 RESERVED가 된 순간

	@ManyToOne
	@JoinColumn(name = "stay", foreignKey = @ForeignKey(
		name = "fk_Reservation_Stay",
		foreignKeyDefinition = "foreign key (stay) references Stay(id) on delete set null"))
	private Stay stay;

	@ManyToOne
	@JoinColumn(name = "member", foreignKey = @ForeignKey(
		name = "fk_Reservation_Member",
		foreignKeyDefinition = "foreign key (member) references Member(id) on delete set null"))
	private Member member;
}
