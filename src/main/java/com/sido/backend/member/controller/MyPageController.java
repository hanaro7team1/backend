package com.sido.backend.member.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sido.backend.member.dto.MyPageResponseDTO;
import com.sido.backend.member.dto.PasswordUpdateRequestDTO;
import com.sido.backend.member.dto.PhoneUpdateRequestDTO;
import com.sido.backend.member.service.MemberService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/mypage")
@Tag(name = "마이페이지")
public class MyPageController {

	private final MemberService memberService;

	@Operation(summary = "Host 마이페이지 정보 조회")
	@GetMapping("/host")
	public ResponseEntity<MyPageResponseDTO> getHostMyPageInfo(@AuthenticationPrincipal User user) {
		MyPageResponseDTO myPageInfo = memberService.getMyPageInfo(user.getUsername());
		return ResponseEntity.ok(myPageInfo);
	}

	@Operation(summary = "Host 전화번호 변경")
	@PatchMapping("/host/phone")
	public ResponseEntity<Void> updateHostPhone(
		@AuthenticationPrincipal User user,
		@Valid @RequestBody PhoneUpdateRequestDTO request) {
		memberService.updatePhone(user.getUsername(), request);
		return ResponseEntity.ok().build();
	}

	@Operation(summary = "Host 비밀번호 변경")
	@PatchMapping("/host/password")
	public ResponseEntity<Void> updateHostPassword(
		@AuthenticationPrincipal User user,
		@Valid @RequestBody PasswordUpdateRequestDTO request) {
		memberService.updatePassword(user.getUsername(), request);
		return ResponseEntity.ok().build();
	}
}
