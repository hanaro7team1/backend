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
		"/api/users/signin", // spring security
		"/api/users/signup", // spring security
		"/actuator/**",
		"/swagger-ui/**",
		"/sido/api-docs/**",
		"/api/members/signin",
		"/api/members/signup"
	};

	@Override
	protected boolean shouldNotFilter(@NonNull HttpServletRequest request) throws ServletException {
		String path = request.getRequestURI();
		System.out.println("*** [JWT] shouldNotFilter path = " + path);
		boolean isNotNeed = Arrays.stream(excludePatterns)
			.anyMatch(pattern -> pathMatcher.match(pattern, path));
		System.out.println("isNotNeed = " + isNotNeed);
		return isNotNeed;
	}

	@Override
	protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
		@NonNull FilterChain filterChain) throws ServletException, IOException {
		System.out.println("*** [JWT} doFilterInternal path = " + request.getRequestURI());
		String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

		// 토큰이 없거나 Bearer로 시작하지 않으면 다음 필터로 넘김
		// SecurityConfig : http.addFilterBefore(new JwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
		// JwtAuthenticationFilter -> UsernamePasswordAuthenticationFilter (인증 필터) -> authorizeHttpRequests (인가 설정)
		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			filterChain.doFilter(request, response);
			return;
		}

		try {
			// 토큰이 있을 때만 검증
			Map<String, Object> claims = JwtUtil.validateToken(authHeader.substring(7)); // Bearer 빼고 토큰 부분만

			Object rawId = claims.get("memberId");
			Long memberId = (rawId instanceof Number) ? ((Number)rawId).longValue() : null;
			// Number 인터페이스로 캐스팅한 후 longValue() 호출
			// -> Integer, Long, BigDecimal 등 어떤 숫자 래퍼라도 안전하게 long으로 변환

			// Long memberId = (long)claims.get("memberId");
			// 해당 오브젝트가 실제로 Long일 때에만 언박싱 가능. Integer로 판별나면 long으로 변환 불가능
			String loginId = (String)claims.get("loginId");
			String role = (String)claims.get("role");
			String displayName = (String)claims.get("name");

			MemberDTO dto = new MemberDTO(memberId, loginId, "", role, displayName); // 빈 문자열로 비번 등록
			UsernamePasswordAuthenticationToken authenticationToken = new
				UsernamePasswordAuthenticationToken(dto, null, dto.getAuthorities());

			// 올바른 Authorization을 저장하여 어디서든 불러올 수 있다!
			SecurityContextHolder.getContext().setAuthentication(authenticationToken);

			System.out.println("*** [JWT] setAuthentication principal = " + dto.getClass().getName());

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
