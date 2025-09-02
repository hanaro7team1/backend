package com.sido.backend.member.service;

import com.sido.backend.member.dto.MyPageResponseDTO;
import com.sido.backend.member.dto.PasswordUpdateRequestDTO;
import com.sido.backend.member.dto.PhoneUpdateRequestDTO;

public interface MemberService {
	MyPageResponseDTO getMyPageInfo(Long memberId);

	void updatePhone(Long memberId, PhoneUpdateRequestDTO request);

	void updatePassword(Long memberId, PasswordUpdateRequestDTO request);
}
