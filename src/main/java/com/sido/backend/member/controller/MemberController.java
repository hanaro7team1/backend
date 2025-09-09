package com.sido.backend.member.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.CookieValue;
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
			String accessToken = JwtUtil.generateToken(claims, 60 * 3); //3시간
			String refreshToken = JwtUtil.generateToken(claims, 60 * 10); //10시간
			String role = (String)claims.get("role");

			// httpOnly 쿠키로 토큰 설정
			Cookie accessCookie = new Cookie("accessToken", accessToken);
			accessCookie.setHttpOnly(true);
			accessCookie.setSecure(false);
			accessCookie.setPath("/");
			accessCookie.setMaxAge(60 * 60 * 3); // 3시간

			Cookie refreshCookie = new Cookie("refreshToken", refreshToken);
			refreshCookie.setHttpOnly(true);
			refreshCookie.setSecure(false);
			refreshCookie.setPath("/");
			refreshCookie.setMaxAge(60 * 60 * 10); // 10시간

			Cookie roleCookie = new Cookie("role", role);
			roleCookie.setHttpOnly(true);
			roleCookie.setSecure(false);
			roleCookie.setPath("/");
			roleCookie.setMaxAge(60 * 60 * 3); // 3시간

			response.addCookie(accessCookie);
			response.addCookie(refreshCookie);
			response.addCookie(roleCookie);

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

	@Operation(summary = "토큰 갱신")
	@PostMapping("/refresh")
	public ResponseEntity<?> refresh(
		@CookieValue(value = "refreshToken", required = false) String refreshToken,
		HttpServletResponse response) {

		System.out.println("REFRESH CONTROLLER ****** refreshToken: " + refreshToken);

		if (refreshToken == null || refreshToken.isEmpty()) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Refresh token이 없습니다.");
		}

		try {
			// refreshToken 검증
			Map<String, Object> claims = JwtUtil.validateToken(refreshToken);

			// 새로운 accessToken과 refreshToken 생성
			String newAccessToken = JwtUtil.generateToken(claims, 60); //1시간
			String newRefreshToken = JwtUtil.generateToken(claims, 600); //10시간
			String role = (String)claims.get("role"); // role 정보 추출

			System.out.println("REFRESH CONTROLLER ****** - newAccessToken: " + newAccessToken);
			System.out.println("REFRESH CONTROLLER ****** - newRefreshToken: " + newRefreshToken);
			System.out.println("REFRESH CONTROLLER ****** - role: " + role);

			// 새로운 쿠키 설정
			Cookie newAccessCookie = new Cookie("accessToken", newAccessToken);
			newAccessCookie.setHttpOnly(true);
			newAccessCookie.setSecure(false);
			newAccessCookie.setPath("/");
			newAccessCookie.setMaxAge(60 * 60 * 3); // 3시간

			Cookie newRefreshCookie = new Cookie("refreshToken", newRefreshToken);
			newRefreshCookie.setHttpOnly(true);
			newRefreshCookie.setSecure(false);
			newRefreshCookie.setPath("/");
			newRefreshCookie.setMaxAge(60 * 10 * 10); // 10시간

			// role 쿠키도 새로 설정
			Cookie newRoleCookie = new Cookie("role", role);
			newRoleCookie.setHttpOnly(true);
			newRoleCookie.setSecure(false);
			newRoleCookie.setPath("/");
			newRoleCookie.setMaxAge(60 * 60 * 3); // 3시간

			response.addCookie(newAccessCookie);
			response.addCookie(newRefreshCookie);
			response.addCookie(newRoleCookie);

			System.out.println("REFRESH CONTROLLER ****** try - response: " + response);

			// 응답 본문에 role 정보 포함
			return ResponseEntity.ok().body(Map.of(
				"message", "토큰이 갱신되었습니다.",
				"role", role
			));

		} catch (Exception e) {
			// refresh 토큰이 만료되거나 유효하지 않은 경우
			// 모든 쿠키 삭제
			Cookie accessCookie = new Cookie("accessToken", "");
			accessCookie.setHttpOnly(true);
			accessCookie.setSecure(false);
			accessCookie.setPath("/");
			accessCookie.setMaxAge(0);

			Cookie refreshCookie = new Cookie("refreshToken", "");
			refreshCookie.setHttpOnly(true);
			refreshCookie.setSecure(false);
			refreshCookie.setPath("/");
			refreshCookie.setMaxAge(0);

			Cookie roleCookie = new Cookie("role", "");
			roleCookie.setHttpOnly(true);
			roleCookie.setSecure(false);
			roleCookie.setPath("/");
			roleCookie.setMaxAge(0);

			response.addCookie(accessCookie);
			response.addCookie(refreshCookie);
			response.addCookie(roleCookie);

			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Refresh token이 만료되었습니다. 다시 로그인해주세요.");
		}
	}

	@Operation(summary = "사용자 로그아웃")
	@PostMapping("/signout")
	public ResponseEntity<?> logout(HttpServletResponse response) {
		Cookie accessCookie = new Cookie("accessToken", "");
		accessCookie.setHttpOnly(true);
		accessCookie.setSecure(false);
		accessCookie.setPath("/");
		accessCookie.setMaxAge(0);

		Cookie refreshCookie = new Cookie("refreshToken", "");
		refreshCookie.setHttpOnly(true);
		refreshCookie.setSecure(false);
		refreshCookie.setPath("/");
		refreshCookie.setMaxAge(0);

		Cookie roleCookie = new Cookie("role", "");
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
