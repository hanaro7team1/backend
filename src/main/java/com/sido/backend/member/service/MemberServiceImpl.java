package com.sido.backend.member.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sido.backend.member.dto.MyPageResponseDTO;
import com.sido.backend.member.dto.PasswordUpdateRequestDTO;
import com.sido.backend.member.dto.PhoneUpdateRequestDTO;
import com.sido.backend.member.entity.HostMember;
import com.sido.backend.member.repository.HostMemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberServiceImpl implements MemberService {

	private final HostMemberRepository hostMemberRepository;
	private final PasswordEncoder passwordEncoder;

	@Override
	public MyPageResponseDTO getMyPageInfo(String loginId) {
		HostMember hostMember = hostMemberRepository.findByLoginId(loginId)
			.orElseThrow(() -> new IllegalArgumentException("해당 아이디를 찾을 수 없습니다: " + loginId));
		return MyPageResponseDTO.builder()
			.villageName(hostMember.getVillageName())
			.phone(hostMember.getPhone())
			.build();
	}

	@Override
	@Transactional
	public void updatePhone(String loginId, PhoneUpdateRequestDTO request) {
		HostMember hostMember = hostMemberRepository.findByLoginId(loginId)
			.orElseThrow(() -> new IllegalArgumentException("해당 아이디를 찾을 수 없습니다: " + loginId));
		hostMember.setPhone(request.getPhone());
		hostMemberRepository.save(hostMember);
	}

	@Override
	@Transactional
	public void updatePassword(String loginId, PasswordUpdateRequestDTO request) {
		HostMember hostMember = hostMemberRepository.findByLoginId(loginId)
			.orElseThrow(() -> new IllegalArgumentException("해당 아이디를 찾을 수 없습니다: " + loginId));

		if (!passwordEncoder.matches(request.getCurrentPassword(), hostMember.getPassword())) {
			throw new IllegalArgumentException("현재 비밀번호가 일치하지 않습니다.");
		}

		hostMember.setPassword(passwordEncoder.encode(request.getNewPassword()));
		hostMemberRepository.save(hostMember);
	}
}
