package com.sido.backend.member.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sido.backend.member.dto.LoginRequestDTO;
import com.sido.backend.security.JwtUtil;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
@Tag(name = "사용자")
public class MemberController {
	private final AuthenticationManager authenticationManager;

	@Operation(summary = "사용자 로그인")
	@PostMapping("/signin")
	public ResponseEntity<?> login(@Valid @RequestBody LoginRequestDTO loginRequestDTO, HttpServletResponse response) {
		try {
			// AuthenticationManager -> AuthenticationProvider로 요청 전달
			// -> DaoAuthenticationProvider 내부에서
			// UserDetails user = this.userDetailsService.loadUserByUsername(username); 호출
			Authentication authenticate = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(
					loginRequestDTO.loginId(), loginRequestDTO.password()
				)
			);

			Map<String, Object> claims = JwtUtil.authenticationToClaims(authenticate);
			String accessToken = (String) claims.get("accessToken");
			String refreshToken = (String) claims.get("refreshToken");
			String role = (String) claims.get("role");

			// httpOnly 쿠키로 토큰 설정
			Cookie accessCookie = new Cookie("accessToken", accessToken);
			accessCookie.setHttpOnly(true);
			accessCookie.setSecure(false); // 로컬 개발환경이므로 false, 프로덕션에서는 true
			accessCookie.setPath("/");
			accessCookie.setMaxAge(60 * 60 * 3); // 3시간

			Cookie refreshCookie = new Cookie("refreshToken", refreshToken);
			refreshCookie.setHttpOnly(true);
			refreshCookie.setSecure(false);
			refreshCookie.setPath("/");
			refreshCookie.setMaxAge(600 * 60); // 600분

			Cookie roleCookie = new Cookie("role", role);
			roleCookie.setHttpOnly(true);
			roleCookie.setSecure(false);
			roleCookie.setPath("/");
			roleCookie.setMaxAge(60 * 60 * 3); // 3시간

			response.addCookie(accessCookie);
			response.addCookie(refreshCookie);
			response.addCookie(roleCookie);

			// 토큰 없이 사용자 정보만 반환
			Map<String, Object> userInfo = new HashMap<>();
			userInfo.put("memberId", claims.get("memberId"));
			userInfo.put("loginId", claims.get("loginId"));
			userInfo.put("role", claims.get("role"));
			userInfo.put("name", claims.get("name"));

			return ResponseEntity.ok(userInfo);

		} catch (AuthenticationException e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("아이디 또는 비밀번호가 올바르지 않습니다.");
		}
	}

	@Operation(summary = "사용자 로그아웃")
	@PostMapping("/signout")
	public ResponseEntity<?> logout(HttpServletResponse response) {
		// 쿠키 삭제
		Cookie accessCookie = new Cookie("accessToken", "");
		accessCookie.setHttpOnly(true);
		accessCookie.setSecure(false);
		accessCookie.setPath("/");
		accessCookie.setMaxAge(0); // 즉시 만료

		Cookie refreshCookie = new Cookie("refreshToken", "");
		refreshCookie.setHttpOnly(true);
		refreshCookie.setSecure(false);
		refreshCookie.setPath("/");
		refreshCookie.setMaxAge(0);

		Cookie roleCookie = new Cookie("role", "role");
		roleCookie.setHttpOnly(true);
		roleCookie.setSecure(false);
		roleCookie.setPath("/");
		roleCookie.setMaxAge(0);

		response.addCookie(accessCookie);
		response.addCookie(refreshCookie);
		response.addCookie(roleCookie);

		return ResponseEntity.ok("로그아웃되었습니다.");
	}
}
