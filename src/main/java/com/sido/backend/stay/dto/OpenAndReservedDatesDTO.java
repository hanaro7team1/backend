package com.sido.backend.stay.dto;

import java.time.LocalDate;
import java.util.List;

public record OpenAndReservedDatesDTO(
	List<LocalDate> openDates,
	List<LocalDate> reservedDates
) {
}
