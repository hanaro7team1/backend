package com.sido.backend.stay.dto;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OpenAndReservedDatesDTO {
	private YearMonth yearMonth;
	private List<LocalDate> openDates;
	private List<LocalDate> reservedDates;
	private boolean hasOpenPrev;
	private boolean hasOpenNext;
	private boolean hasReservedPrev;
	private boolean hasReservedNext;
}
