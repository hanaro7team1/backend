package com.sido.backend.stay.dto;

import com.fasterxml.jackson.annotation.JsonUnwrapped;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class StayUpdateDTO {
	@Valid
	@JsonUnwrapped
	private StaySpecDTO staySpec;

	//헬퍼 메서드 추가 (get을 두 번씩이나 부르지 않도록)
	public Integer capacity() {
		return staySpec.capacity();
	}

	public Integer areaSize() {
		return staySpec.areaSize();
	}

	public String description() {
		return staySpec.description();
	}
}