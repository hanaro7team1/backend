package com.sido.backend.member.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.sido.backend.member.dto.MemberDTO;
import com.sido.backend.member.entity.HostMember;
import com.sido.backend.member.entity.Member;
import com.sido.backend.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@RequiredArgsConstructor
@Log4j2
public class CustomUserDetailsService implements UserDetailsService {
	private final MemberRepository memberRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Member m = memberRepository.findByLoginId(username).orElseThrow(
			() -> new UsernameNotFoundException(username + "is not found")
		);

		String displayName = (m instanceof HostMember hm) ? hm.getVillageName() : m.getName();
		MemberDTO memberDTO = new MemberDTO(
			m.getId(),
			m.getLoginId(),
			m.getPassword(),
			m.getRole().name(),
			displayName
		);

		log.info(memberDTO.toString());

		return memberDTO;
	}
}
