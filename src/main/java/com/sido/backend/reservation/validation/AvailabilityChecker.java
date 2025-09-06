package com.sido.backend.reservation.validation;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import org.springframework.stereotype.Component;

import com.sido.backend.common.exception.ConflictException;
import com.sido.backend.common.exception.ResourceGoneException;
import com.sido.backend.stay.entity.Stay;
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
			throw new ConflictException("선택한 기간에 예약 불가 날짜가 포함되어 있습니다.");
		}
	}

	public void assertStayIsActive(Stay stay) {
		if (stay.getIsActive().equals(false)) {
			throw new ResourceGoneException("해당 사랑방은 삭제되어 더 이상 예약할 수 없습니다.");
		}
	}
}
