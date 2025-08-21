package com.sido.backend.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sido.backend.member.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
