package com.sido.backend.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record LoginRequestDTO(
	@NotBlank(message = "아이디를 입력해주세요.")
	@Size(min = 1, max = 50, message = "아이디는 1~50자 이내여야 합니다.")
	@Schema(name = "loginId", example = "host1")
	String loginId,

	@NotBlank(message = "비밀번호를 입력해주세요.")
	@Size(min = 8, max = 20, message = "비밀번호는 8~20자 이내여야 합니다.")
	@Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-={}\\[\\]:;\"'<>,.?/]).*$",
		message = "비밀번호는 영문자, 숫자, 특수문자를 각각 최소 1개 이상 포함해야 합니다.")
	@Schema(name = "password", example = "password123!")
	String password
) {
}
