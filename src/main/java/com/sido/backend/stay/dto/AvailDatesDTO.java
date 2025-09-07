package com.sido.backend.stay.dto;

import java.time.LocalDate;
import java.util.List;

public record AvailDatesDTO(
	List<LocalDate> dates
) {
}
