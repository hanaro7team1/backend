package com.sido.backend.member.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sido.backend.member.entity.HostMember;

public interface HostMemberRepository extends JpaRepository<HostMember, Long> {
	Optional<HostMember> findByLoginId(String loginId);
}
