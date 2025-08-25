package com.sido.backend.house.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
public class HouseAvailableDate {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private LocalDate availableDate;

	@ManyToOne
	@JoinColumn(name = "house",
		foreignKey = @ForeignKey(
			name = "fk_HouseAvailableDate_House",
			foreignKeyDefinition = """
					foreign key (house)
					   references House(id)
					    on DELETE cascade on UPDATE cascade
				"""
		)
	)
	private House house;
}
