package com.sido.backend.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter

public class SignUpRequestDTO {
	@Schema(description = "아이디", example = "hana_user01")
	@NotBlank(message = "아이디는 공백일 수 없습니다")
	@Size(min = 5, max = 20, message = "아이디는 최소 5자, 최대 20자 이하여야 합니다.")
	@Pattern(
		regexp = "^[a-z0-9_]+$",
		message = "아이디는 영문 소문자, 숫자, 밑줄(_)만 사용할 수 있습니다."
	)
	String loginId;

	@Schema(description = "비밀번호", example = "Abcdef12!")
	@NotBlank(message = "비밀번호는 공백일 수 없습니다.")
	@Size(min = 8, max = 20, message = "비밀번호의 길이가 8자 이상 20자 이하여야 합니다.")
	@Pattern(
		regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#$%^&*]).{8,20}$",
		message = "비밀번호는 영문자, 숫자, 특수문자를 모두 포함해야 합니다."
	)
	String password;

	@Schema(example = "하나마을")
	@NotBlank
	String villageName;

	@Schema(example = "전라남도 해남")
	@NotBlank
	String region;

	@Schema(example = "010-1234-5678")
	@NotBlank
	@Pattern(regexp = "^0\\d{1,2}-\\d{3,4}-\\d{4}$", message = "전화번호 양식이 올바르지 않습니다.")
	String phone;

}