package com.sido.backend.member.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PhoneUpdateRequestDTO {
	@NotBlank
	private String phone;
}
