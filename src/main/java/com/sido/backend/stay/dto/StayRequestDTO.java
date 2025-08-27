package com.sido.backend.stay.dto;

import com.sido.backend.stay.entity.Stay;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class StayRequestDTO extends StayUpdateDTO {
	@NotBlank
	@Size(min = 1, max = 64)
	private String title;

	@NotBlank
	@Size(min = 1, max = 64)
	private String address;

	@NotBlank
	@Size(min = 1, max = 9)
	private String ownerName;

	@NotBlank
	@Size(min = 1, max = 31)
	@Pattern(regexp = "^0\\d{1,2}-\\d{3,4}-\\d{4}$", message = "전화번호 양식이 올바르지 않습니다.")
	private String ownerPhone;

	public Stay toEntity() {
		return Stay.builder()
			.isHomestay(true)
			.address(address)
			.capacity(getCapacity())
			.areaSize(getAreaSize())
			.ownerName(ownerName)
			.ownerPhone(ownerPhone)
			.description(getDescription())
			.build();
	}
}