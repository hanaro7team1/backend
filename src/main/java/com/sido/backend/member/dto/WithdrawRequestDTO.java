package com.sido.backend.member.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WithdrawRequestDTO {
	@NotBlank(message = "비밀번호를 입력해 주세요")
	private String checkPassword;
}
