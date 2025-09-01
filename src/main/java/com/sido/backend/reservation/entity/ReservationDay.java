package com.sido.backend.reservation.entity;

import java.time.LocalDate;

import com.sido.backend.stay.entity.Stay;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(uniqueConstraints = {
	@UniqueConstraint(name = "uk_ReservationDay_stay_date", columnNames = {"stay", "date"})
})
public class ReservationDay {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private LocalDate date;

	@ManyToOne
	@JoinColumn(name = "reservation", foreignKey = @ForeignKey(
		name = "fk_reservationDay_reservation",
		foreignKeyDefinition = "foreign key (reservation) references Reservation(id) on delete cascade"
	))
	private Reservation reservation;

	@ManyToOne
	@JoinColumn(name = "stay", foreignKey = @ForeignKey(
		name = "fk_reservationDay_stay",
		foreignKeyDefinition = "foreign key (stay) references Stay(id) on delete set null"
	))
	private Stay stay;
}
