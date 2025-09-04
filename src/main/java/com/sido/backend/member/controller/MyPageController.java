package com.sido.backend.member.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sido.backend.member.dto.MemberDTO;
import com.sido.backend.member.dto.MyPageResponseDTO;
import com.sido.backend.member.dto.PhoneUpdateRequestDTO;
import com.sido.backend.member.dto.WithdrawRequestDTO;
import com.sido.backend.member.service.MemberService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
@Tag(name = "마이페이지")
public class MyPageController {

	private final MemberService memberService;

	@Operation(summary = "Host 마이페이지 정보 조회")
	@GetMapping("/mypage")
	public ResponseEntity<MyPageResponseDTO> getHostMyPageInfo(@AuthenticationPrincipal MemberDTO memberDTO) {
		MyPageResponseDTO myPageInfo = memberService.getMyPageInfo(memberDTO.getMemberId());
		return ResponseEntity.ok(myPageInfo);
	}

	@Operation(summary = "Host 전화번호 변경")
	@PatchMapping("/mypage/phone")
	public ResponseEntity<Void> updateHostPhone(
		@AuthenticationPrincipal MemberDTO memberDTO,
		@Valid @RequestBody PhoneUpdateRequestDTO request) {
		memberService.updatePhone(memberDTO.getMemberId(), request);
		return ResponseEntity.ok().build();
	}

	@Operation(summary = "Host 탈퇴")
	@PatchMapping("/mypage/quit")
	public ResponseEntity<Void> updateHostQuit(
		@AuthenticationPrincipal MemberDTO memberDTO,
		@Valid @RequestBody WithdrawRequestDTO request) {
		memberService.withdraw(memberDTO.getMemberId(), request);
		return ResponseEntity.ok().build();
	}

}
