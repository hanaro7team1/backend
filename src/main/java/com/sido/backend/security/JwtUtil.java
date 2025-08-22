package com.sido.backend.security;

import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.time.ZonedDateTime;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.security.core.Authentication;

import com.sido.backend.member.dto.MemberDTO;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.InvalidClaimException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.WeakKeyException;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class JwtUtil {
	private static final SecretKey KEY = Keys.hmacShaKeyFor(
		"q9oOuXLApcgBxD_lGsLcICoyt8MgXYTzgXBb8AL7Brw=".getBytes(StandardCharsets.UTF_8));

	public static String generateToken(Map<String, Object> valueMap, int min) {
		String jwtStr = Jwts.builder().setHeader(Map.of("typ", "JWT"))
			.setClaims(valueMap) // payload에 전달 받은 claims 추가
			.setIssuedAt(Date.from(ZonedDateTime.now().toInstant())) // 발행시간
			.setExpiration(Date.from(ZonedDateTime.now().plusMinutes(min).toInstant())) // 만료시간
			.signWith(KEY).compact();

		System.out.println("jwtStr = " + jwtStr);
		return jwtStr;
	}

	public static Map<String, Object> validateToken(String token) {
		Map<String, Object> claim;

		try {
			claim = Jwts.parserBuilder()
				.setSigningKey(KEY)
				.build()
				.parseClaimsJws(token)
				.getBody(); // 키 가져와서 parsing 즉, 복호화
		} catch (WeakKeyException e) {
			throw new CustomJwtException("WeakKeyException");
		} catch (MalformedJwtException e) {
			throw new CustomJwtException("MalformedJwtException");
		} catch (ExpiredJwtException e) {
			throw new CustomJwtException("ExpiredJwtException");
		} catch (InvalidClaimException e) {
			throw new CustomJwtException("InvalidClaimException");
		} catch (JwtException e) {
			throw new CustomJwtException("JwtException");
		} catch (Exception e) {
			throw new CustomJwtException("UnknownException");
		}

		return claim;
	}

	public static Map<String, Object> authenticationToClaims(Authentication authentication) {
		MemberDTO dto = (MemberDTO)authentication.getPrincipal();
		MemberDTO memberDTO = new MemberDTO(
			dto.getLoginId(), "", dto.getRole(), dto.getName(), dto.getVillageName());

		Map<String, Object> claims = memberDTO.getClaims();

		claims.put("accessToken", JwtUtil.generateToken(claims, 10));
		claims.put("refreshToken", JwtUtil.generateToken(claims, 600));
		return claims;
	}
}
