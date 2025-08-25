package com.sido.backend.reservation.entity;

import java.time.LocalDate;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.sido.backend.common.entity.BaseEntity;
import com.sido.backend.stay.entity.Stay;
import com.sido.backend.member.entity.Member;

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
import jakarta.persistence.OneToOne;
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
	private short personCnt;

	@Column(nullable = false)
	private Boolean isFarm;

	@Enumerated(EnumType.STRING)
	private ResrvStatus resrvStatus;

	@OneToOne(optional = false)
	@JoinColumn(name = "house",
		foreignKey = @ForeignKey(name = "fk_Reservation_House"))
	@OnDelete(action = OnDeleteAction.SET_NULL)
	private Stay house;

	@ManyToOne
	@JoinColumn(name = "member",
		foreignKey = @ForeignKey(name = "fk_Reservation_Member"))
	@OnDelete(action = OnDeleteAction.SET_NULL)
	private Member member;
}
