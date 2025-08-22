package com.sido.backend.security;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sido.backend.member.dto.MemberDTO;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtAuthenticationFilter extends OncePerRequestFilter {
	private final AntPathMatcher pathMatcher = new AntPathMatcher(); // /** 처럼 n depth 체크해줄 수 있도록

	private final String[] excludePatterns = { // 로그인 체크 안 하는 도메인
		"/api/members/login", // spring security
		"/api/members/signup", // spring security
		"/api/public/**",
		"/actuator/**",
		"/swagger-ui/**",
		"/sido/api-docs/**",
		"/members/signUp",
		"/members/login"
	};

	@Override
	protected boolean shouldNotFilter(@NonNull HttpServletRequest request) throws ServletException {
		String path = request.getRequestURI();
		System.out.println("** path = " + path);
		boolean isNotNeed = Arrays.stream(excludePatterns)
			.anyMatch(pattern -> pathMatcher.match(pattern, path));
		System.out.println("isNotNeed = " + isNotNeed);
		return isNotNeed;
	}

	@Override
	protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
		@NonNull FilterChain filterChain) throws ServletException, IOException {
		System.out.println("*** JwtAuthenticationFilter.doFilterInternal");
		String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

		try {
			Map<String, Object> claims = JwtUtil.validateToken(authHeader.substring(7)); // Bearer 빼고 토큰 부분만

			String loginId = (String)claims.get("loginId");
			String role = (String)claims.get("role");
			String displayName = (String)claims.get("name");

			MemberDTO dto = new MemberDTO(loginId, "", role, displayName); // 빈 문자열로 비번 등록
			UsernamePasswordAuthenticationToken authenticationToken = new
				UsernamePasswordAuthenticationToken(dto, null, dto.getAuthorities());

			// 올바른 Authorization을 저장하여 어디서든 불러올 수 있다!
			SecurityContextHolder.getContext().setAuthentication(authenticationToken);

		} catch (Exception e) {
			response.setContentType("application/json");
			ObjectMapper objectMapper = new ObjectMapper();
			PrintWriter out = response.getWriter();
			out.println(objectMapper.writeValueAsString(Map.of("error", "ERROR_ACCESS_TOKEN")));
			out.close();
		}

		filterChain.doFilter(request, response);
	}
}
