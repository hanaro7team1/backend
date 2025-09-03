package com.sido.backend.member.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordUpdateRequestDTO {
	@NotBlank(message = "기존 비밀번호를 입력해 주세요")
	private String currentPassword;

	@NotBlank(message = "변경할 비밀번호를 입력해 주세요")
	@Size(min = 8, max = 20, message = "영문자,숫자,특수문자를 포함한 8~20자")
	@Pattern(
		regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*])[A-Za-z\\d!@#$%^&*]{8,20}$",
		message = "변경할 비밀번호는 영문자,숫자,특수문자를 포함한 8~20자여야 합니다"
	)
	private String newPassword;
}
