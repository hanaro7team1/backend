package com.sido.backend.reservation.dto;

import com.fasterxml.jackson.annotation.JsonValue;
import com.sido.backend.reservation.entity.ResrvStatus;
import com.sido.backend.reservation.entity.VisitStatus;

public enum ReservationViewStatus {
	UPCOMING("방문 전"),
	IN_PROGRESS("방문 중"),
	COMPLETED("방문 완료"),
	CANCELLED("예약 취소");

	private final String label;

	ReservationViewStatus(String label) {
		this.label = label;
	}

	public static ReservationViewStatus from(ResrvStatus resrv, VisitStatus visit) {
		if (visit == VisitStatus.UPCOMING)
			return UPCOMING;
		if (visit == VisitStatus.IN_PROGRESS)
			return IN_PROGRESS;
		if (visit == VisitStatus.COMPLETED)
			return COMPLETED;
		if (resrv == ResrvStatus.CANCELLED)
			return CANCELLED;
		throw new IllegalStateException("Unexpected status: " + resrv + "/" + visit);
	}

	@JsonValue // 직렬화 시 label을 반환
	public String getLabel() {
		return label;
	}
}
