package com.sido.backend.member.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MyPageResponseDTO {
	private String villageName;
	private String phone;
}
