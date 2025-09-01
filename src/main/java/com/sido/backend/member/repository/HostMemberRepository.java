package com.sido.backend.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sido.backend.member.entity.HostMember;

import java.util.Optional;

public interface HostMemberRepository extends JpaRepository<HostMember, Long> {
    Optional<HostMember> findByLoginId(String loginId);
}
