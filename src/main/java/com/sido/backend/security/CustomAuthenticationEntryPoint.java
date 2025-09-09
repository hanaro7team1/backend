package com.sido.backend.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.web.AuthenticationEntryPoint;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;

import java.io.IOException;
import java.util.Map;

public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
	@Override
	public void commence(HttpServletRequest request,
		HttpServletResponse response,
		AuthenticationException authException) throws IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		response.setContentType("application/json");
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401
		response.getWriter().write(
			objectMapper.writeValueAsString(Map.of("error", "ERROR_UNAUTHORIZED"))
		);
	}
}
