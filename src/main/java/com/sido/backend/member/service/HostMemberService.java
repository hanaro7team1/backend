package com.sido.backend.member.service;

import com.sido.backend.member.dto.SignUpRequestDTO;
import com.sido.backend.member.entity.HostMember;

public interface HostMemberService {
	boolean isLoginIdTaken(String loginId);

	HostMember signup(SignUpRequestDTO dto);
}