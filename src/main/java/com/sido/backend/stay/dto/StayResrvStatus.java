package com.sido.backend.stay.dto;

import com.fasterxml.jackson.annotation.JsonValue;

public enum StayResrvStatus {
	AVAILABLE("예약 가능"),
	SOLD_OUT("예약 마감"),
	CLOSED("예약 닫힘");

	private final String label;

	StayResrvStatus(String label) {
		this.label = label;
	}

	@JsonValue // 직렬화 시 label을 반환
	public String getLabel() {
		return label;
	}
}
