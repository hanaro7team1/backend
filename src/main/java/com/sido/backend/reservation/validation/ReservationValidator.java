package com.sido.backend.reservation.validation;

import java.time.LocalDate;

import org.springframework.stereotype.Component;

import com.sido.backend.reservation.entity.Reservation;
import com.sido.backend.reservation.entity.ResrvStatus;
import com.sido.backend.stay.entity.Stay;

@Component
public class ReservationValidator {
	public void assertCoreRules(Stay stay, LocalDate start, LocalDate end, Integer cnt) {
		LocalDate today = LocalDate.now();
		if (Boolean.FALSE.equals(stay.getIsActive())) {
			throw new IllegalArgumentException("해당 사랑방은 예약이 닫혔습니다.");
		}
		if (start.isBefore(today)) {
			throw new IllegalArgumentException("지난 날짜는 예약할 수 없습니다.");
		}
		if (end.isBefore(start)) {
			throw new IllegalArgumentException("종료일은 시작일보다 앞설 수 없습니다.");
		}
		if (start.equals(end)) {
			throw new IllegalArgumentException("당일치기는 불가능합니다.");
		}
		if (cnt == null || cnt < 1) {
			throw new IllegalArgumentException("인원 수가 올바르지 않습니다.");
		}
		if (cnt > stay.getCapacity()) {
			throw new IllegalArgumentException("예약 가능한 인원수를 초과하였습니다.");
		}
	}

	public void assertDatesPairOrNone(LocalDate start, LocalDate end) {
		if ((start == null) ^ (end == null)) {
			throw new IllegalArgumentException("startDate와 endDate는 함께 전달하거나 둘 다 생략해야 합니다.");
		}
	}

	public void assertOwnedBy(Reservation reservation, Long memberId) {
		if (!reservation.getMember().getId().equals(memberId)) {
			throw new IllegalArgumentException("본인의 예약만 처리할 수 있습니다.");
		}
	}

	public void assertConfirmable(Reservation reservation) {
		if (reservation.getResrvStatus() != ResrvStatus.PENDING) {
			throw new IllegalArgumentException("현재 상태에서는 예약을 확정할 수 없습니다.");
		}
	}
}
