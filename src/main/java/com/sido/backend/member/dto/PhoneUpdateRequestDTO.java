package com.sido.backend.member.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PhoneUpdateRequestDTO {
	@NotBlank
	@Pattern(regexp = "^0\\d{1,2}-\\d{3,4}-\\d{4}$", message = "전화번호 양식이 올바르지 않습니다.")
	private String phone;
}
