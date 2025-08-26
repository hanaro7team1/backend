package com.sido.backend.stay.entity;

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
public class StayAvailableDate {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private LocalDate availableDate;

	@ManyToOne
	@JoinColumn(name = "stay",
		foreignKey = @ForeignKey(
			name = "fk_StayAvailableDate_Stay",
			foreignKeyDefinition = """
					foreign key (stay)
					   references Stay(id)
					    on DELETE cascade on UPDATE cascade
				"""
		)
	)
	private Stay stay;
}
