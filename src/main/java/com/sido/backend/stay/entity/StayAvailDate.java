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
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
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
@Table(uniqueConstraints = {
	@UniqueConstraint(name = "uk_stay_date", columnNames = {"stay", "availableDate"})
})
public class StayAvailDate {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private LocalDate availableDate;

	@Column(nullable = false)
	@Builder.Default
	private Boolean isAvailable = true;

	@ManyToOne
	@JoinColumn(name = "stay",
		foreignKey = @ForeignKey(
			name = "fk_StayAvailDate_Stay",
			foreignKeyDefinition = """
					foreign key (stay)
					   references Stay(id)
					    on DELETE cascade on UPDATE cascade
				"""
		)
	)
	private Stay stay;
}
