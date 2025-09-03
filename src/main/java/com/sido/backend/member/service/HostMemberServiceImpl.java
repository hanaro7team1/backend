package com.sido.backend.member.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sido.backend.member.dto.SignUpRequestDTO;
import com.sido.backend.member.entity.HostMember;
import com.sido.backend.member.entity.MemberRole;
import com.sido.backend.member.repository.HostMemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class HostMemberServiceImpl implements HostMemberService {
	private final HostMemberRepository hostMemberRepository;

	private final PasswordEncoder passwordEncoder;

	@Override
	public boolean isLoginIdTaken(String loginId) {
		return hostMemberRepository.existsByLoginId(loginId);
	}

	@Override
	public HostMember signup(SignUpRequestDTO dto) {
		if (hostMemberRepository.existsByLoginId(dto.getLoginId())) {
			throw new IllegalArgumentException("이미 존재하는 아이디입니다.");
		}

		String encodedPassword = passwordEncoder.encode(dto.getPassword());

		HostMember hostMember = HostMember.builder()
			.loginId(dto.getLoginId())
			.password(encodedPassword)
			.villageName(dto.getVillageName())
			.region(dto.getRegion())
			.phone(dto.getPhone())
			.stayCount(0)
			.role(MemberRole.ROLE_ADMIN)
			.build();

		return hostMemberRepository.save(hostMember);
	}
}