package com.sido.backend.stay.dto;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.sido.backend.stay.entity.Stay;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class StayCreateDTO {
	@NotBlank
	@Size(min = 1, max = 64)
	private String address;

	@NotBlank
	@Size(min = 1, max = 64)
	private String detailAddress;

	@NotBlank
	@Size(min = 1, max = 9)
	private String ownerName;

	@Valid
	@JsonUnwrapped
	private StaySpecDTO staySpec;

	@NotBlank
	@Size(min = 1, max = 31)
	@Pattern(regexp = "^0\\d{1,2}-\\d{3,4}-\\d{4}$", message = "전화번호 양식이 올바르지 않습니다.")
	private String ownerPhone;

	public Stay toEntity() {
		return Stay.builder()
			.isHomestay(true)
			.address(address)
			.detailAddress(detailAddress)
			.capacity(staySpec.capacity())
			.areaSize(staySpec.areaSize())
			.ownerName(ownerName)
			.ownerPhone(ownerPhone)
			.description(staySpec.description())
			.build();
	}
}
