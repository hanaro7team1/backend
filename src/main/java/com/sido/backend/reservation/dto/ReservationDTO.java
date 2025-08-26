package com.sido.backend.reservation.dto;

import java.time.LocalDate;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ReservationDTO {
	private LocalDate startDate;
	private LocalDate endDate;

	@Schema(example = "2")
	@Builder.Default
	private Integer personCnt = 2;

	@Builder.Default
	private Boolean isFarm = true;
}
