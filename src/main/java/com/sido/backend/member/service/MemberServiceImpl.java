package com.sido.backend.member.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sido.backend.common.exception.BadRequestException;
import com.sido.backend.member.dto.MyPageResponseDTO;
import com.sido.backend.member.dto.PasswordUpdateRequestDTO;
import com.sido.backend.member.dto.PhoneUpdateRequestDTO;
import com.sido.backend.member.dto.WithdrawRequestDTO;
import com.sido.backend.member.entity.HostMember;
import com.sido.backend.member.repository.HostMemberRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberServiceImpl implements MemberService {

	private final HostMemberRepository hostMemberRepository;
	private final PasswordEncoder passwordEncoder;

	@Override
	public MyPageResponseDTO getMyPageInfo(Long memberId) {
		HostMember hostMember = hostMemberRepository.findById(memberId)
			.orElseThrow(() -> new EntityNotFoundException("해당 사용자를 찾을 수 없습니다: " + memberId));
		return MyPageResponseDTO.builder()
			.villageName(hostMember.getVillageName())
			.phone(hostMember.getPhone())
			.build();
	}

	@Override
	@Transactional
	public void updatePhone(Long memberId, PhoneUpdateRequestDTO request) {
		HostMember hostMember = hostMemberRepository.findById(memberId)
			.orElseThrow(() -> new EntityNotFoundException("해당 사용자를 찾을 수 없습니다: " + memberId));

		if (request.getPhone().equals(hostMember.getPhone())) {
			throw new BadRequestException("새로운 연락처는 현재 연락처와 같을 수 없습니다.");
		}

		hostMember.setPhone(request.getPhone());
		hostMemberRepository.save(hostMember);
	}

	@Override
	@Transactional
	public void updatePassword(Long memberId, PasswordUpdateRequestDTO request) {
		HostMember hostMember = hostMemberRepository.findById(memberId)
			.orElseThrow(() -> new EntityNotFoundException("해당 사용자를 찾을 수 없습니다: " + memberId));

		if (!passwordEncoder.matches(request.getCurrentPassword(), hostMember.getPassword())) {
			throw new BadRequestException("현재 비밀번호가 일치하지 않습니다.");
		}

		if (passwordEncoder.matches(request.getNewPassword(), hostMember.getPassword())) {
			throw new BadRequestException("새로운 비밀번호는 현재 비밀번호와 같을 수 없습니다.");
		}

		hostMember.setPassword(passwordEncoder.encode(request.getNewPassword()));
		hostMemberRepository.save(hostMember);
	}

	@Override
	@Transactional
	public void withdraw(Long memberId, WithdrawRequestDTO request) {
		HostMember hostMember = hostMemberRepository.findById(memberId)
			.orElseThrow(() -> new EntityNotFoundException("해당 사용자를 찾을 수 없습니다: " + memberId));

		if (!passwordEncoder.matches(request.getCheckPassword(), hostMember.getPassword())) {
			throw new BadRequestException("비밀번호가 일치하지 않습니다.");
		}

		hostMemberRepository.deleteById(memberId);
	}
}
