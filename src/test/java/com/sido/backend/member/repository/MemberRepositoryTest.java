package com.sido.backend.member.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.sido.backend.member.entity.HostMember;
import com.sido.backend.member.entity.Member;
import com.sido.backend.member.entity.MemberRole;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class MemberRepositoryTest {
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
		Member admin = Member.builder()
			.loginId("admin")
			.password(passwordEncoder.encode("password123!"))
			.name("admin")
			.role(MemberRole.ROLE_ADMIN)
			.phone("010-0000-0000")
			.build();

		Member savedAdmin = memberRepository.save(admin);
		Member foundAdmin = memberRepository.findById(savedAdmin.getId()).orElseThrow();

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
				.role(MemberRole.ROLE_HOST)
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
