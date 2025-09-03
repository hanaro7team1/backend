package com.sido.backend.member.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sido.backend.member.dto.SignUpRequestDTO;
import com.sido.backend.member.entity.HostMember;
import com.sido.backend.member.service.HostMemberService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/host-members")
@Tag(name = "사용자")
public class HostMemberController {
	private final HostMemberService hostMemberService;

	//아이디 중복 체크 api -> 다음 스텝으로 넘어가기 전에 한 번 호출하기
	@GetMapping("/check-id")
	public ResponseEntity<Map<String, Object>> checkLoginId(@RequestParam String loginId) {
		boolean exists = hostMemberService.isLoginIdTaken(loginId);
		return ResponseEntity.ok(Map.of("exists", exists));
	}

	@Operation(summary = "관리자 회원가입")
	@PostMapping("/signup")
	public ResponseEntity<?> signup(@Valid @RequestBody SignUpRequestDTO dto) {
		HostMember saved = hostMemberService.signup(dto);
		return ResponseEntity.ok(Map.of(
			"message", "회원가입이 완료되었습니다.",
			"memberId", saved.getId(),
			"loginId", saved.getLoginId()
		));
	}
}