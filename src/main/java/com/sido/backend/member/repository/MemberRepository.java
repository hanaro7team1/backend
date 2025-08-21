package com.sido.backend.member.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sido.backend.member.entity.Member;
import com.sido.backend.member.entity.MemberRole;

public interface MemberRepository extends JpaRepository<Member, Long> {
	Optional<Member> findByLoginId(String loginId);

	List<Member> findAllByRole(MemberRole role);
}
