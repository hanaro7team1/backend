package com.sido.backend.stay.dto;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AvailDatesDTO {
	private YearMonth yearMonth;
	private List<LocalDate> dates;
	private boolean hasPrev;
	private boolean hasNext;
}
