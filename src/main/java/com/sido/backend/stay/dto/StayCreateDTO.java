package com.sido.backend.stay.dto;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.sido.backend.stay.entity.Stay;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
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
	private String hostName;

	@Valid
	@JsonUnwrapped
	private StaySpecDTO staySpec;

	@NotBlank
	@Size(min = 1, max = 31)
	private String hostPhone;

	public Stay toEntity() {
		return Stay.builder()
			.isHomestay(true)
			.address(address)
			.detailAddress(detailAddress)
			.capacity(staySpec.capacity())
			.areaSize(staySpec.areaSize())
			.ownerName(hostName)
			.ownerPhone(hostPhone)
			.description(staySpec.description())
			.build();
	}
}