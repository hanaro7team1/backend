package com.sido.backend.member.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.sido.backend.RepositoryTest;
import com.sido.backend.member.entity.HostMember;
import com.sido.backend.member.entity.Member;
import com.sido.backend.member.entity.MemberRole;

class MemberRepositoryTest extends RepositoryTest {
	@Autowired
	MemberRepository memberRepository;

	@Autowired
	HostMemberRepository hostMemberRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Test
	@Order(1)
	void saveTest() {
		// admin 생성
		HostMember admin = HostMember.builder()
			.loginId("garam")
			.password(passwordEncoder.encode("password123!"))
			.villageName("가람마을")
			.role(MemberRole.ROLE_ADMIN)
			.phone("055-000-0000")
			.region("경남 창원시")
			.build();

		HostMember savedAdmin = hostMemberRepository.save(admin);
		HostMember foundAdmin = hostMemberRepository.findById(savedAdmin.getId()).orElseThrow();

		assertEquals(savedAdmin, foundAdmin);
	}

	@Test
	@Order(2)
	void addTest() {
		long preHostCnt = hostMemberRepository.count();
		long preUserCnt = memberRepository.findAllByRole(MemberRole.ROLE_USER).size();

		// host 생성
		List<HostMember> hostMembers = Stream.iterate(1, n -> n + 1).limit(3)
			.map(n -> (HostMember)HostMember.builder()
				.loginId("host" + n)
				.password(passwordEncoder.encode("password123!"))
				.role(MemberRole.ROLE_ADMIN)
				.phone("055-111-" + String.format("%04d", n))
				.villageName("village" + n)
				.region("region" + n)
				.build()
			).toList();

		hostMemberRepository.saveAll(hostMembers);

		// 일반 user 생성
		List<Member> users = Stream.iterate(1, n -> n + 1).limit(10)
			.map(n -> (Member)Member.builder()
				.loginId("user" + n)
				.password(passwordEncoder.encode("password123!"))
				.name("user" + n)
				.role(MemberRole.ROLE_USER)
				.phone("010-2222-" + String.format("%04d", n))
				.build()
			).toList();

		memberRepository.saveAll(users);

		assertEquals(preHostCnt + 3, hostMemberRepository.count());
		assertEquals(preUserCnt + 10, memberRepository.findAllByRole(MemberRole.ROLE_USER).size());
	}
}
