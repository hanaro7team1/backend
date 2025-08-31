package com.sido.backend.reservation.validation;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import org.springframework.stereotype.Component;

import com.sido.backend.stay.repository.StayAvailDateRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AvailabilityChecker {
	private final StayAvailDateRepository stayAvailDateRepository;

	public void assertAllDatesAvailable(Long stayId, LocalDate start, LocalDate end) {
		long requestDays = ChronoUnit.DAYS.between(start, end);
		long availableDays = stayAvailDateRepository.countOpenAndUnreservedInRange(stayId, start, end);
		System.out.println("requestDays = " + requestDays);
		System.out.println("availableDays = " + availableDays);
		if (requestDays != availableDays) {
			throw new IllegalArgumentException("선택한 기간에 예약 불가 날짜가 포함되어 있습니다.");
		}
	}
}
