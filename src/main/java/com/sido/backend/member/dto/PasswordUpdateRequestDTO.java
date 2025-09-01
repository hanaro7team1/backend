package com.sido.backend.member.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordUpdateRequestDTO {
	private String currentPassword;
	private String newPassword;
}
