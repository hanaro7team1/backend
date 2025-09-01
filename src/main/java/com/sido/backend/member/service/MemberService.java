package com.sido.backend.member.service;

import com.sido.backend.member.dto.MyPageResponseDTO;
import com.sido.backend.member.dto.PasswordUpdateRequestDTO;
import com.sido.backend.member.dto.PhoneUpdateRequestDTO;

public interface MemberService {
	MyPageResponseDTO getMyPageInfo(String loginId);

	void updatePhone(String loginId, PhoneUpdateRequestDTO request);

	void updatePassword(String loginId, PasswordUpdateRequestDTO request);
}
