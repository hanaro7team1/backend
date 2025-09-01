package com.sido.backend.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.sido.backend.security.CustomAccessDeniedHandler;
import com.sido.backend.security.JwtAuthenticationFilter;
import com.sido.backend.security.LoginFailureHandler;
import com.sido.backend.security.LoginSuccessHandler;

import lombok.extern.log4j.Log4j2;

@Configuration
@Log4j2
@EnableMethodSecurity
public class SecurityConfig {
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http, CorsConfigurationSource corsConfigurationSource) throws
		Exception {
		log.info("***** SecurityConfig - Security Filter Chain *****");

		http
			.csrf(AbstractHttpConfigurer::disable)
			.cors(config -> config.configurationSource(corsConfigurationSource()))
			.sessionManagement(config ->
				config.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.formLogin(form -> form
				.loginPage("/api/users/signin")
				.successHandler(new LoginSuccessHandler()) // loginPage에서 성공하면 LoginSuccessHandler 실행
				.failureHandler(new LoginFailureHandler()) // 실패하면 LoginFailureHandler 실행
			)
			.authorizeHttpRequests(auth -> auth
				// 인가 설정으로, 인증 필터인 UsernamePasswordAuthenticationFilter보다 뒷 순서
				// 로그인, 회원가입, 문서: 공개
				.requestMatchers("/api/users/**", "/api/members/**", "/swagger-ui/**", "/sido/api-docs/**",
					"/actuator/**")
				.permitAll()
				// 공개 API
				.requestMatchers(HttpMethod.GET, "/api/stays/**", "/api/real-estates/**", "/api/festivals/**")
				.permitAll()
				// 인증 필요
				.requestMatchers(HttpMethod.POST, "/api/stays/**", "/api/festivals/**")
				.authenticated()
				.requestMatchers(HttpMethod.PATCH, "/api/festivals/**").authenticated()
				.requestMatchers(HttpMethod.DELETE, "/api/festivals/**").authenticated()
				.requestMatchers("/api/admin/**", "/api/reservations/**", "/api/mypage/**")
				.authenticated())
			.exceptionHandling(config
				-> config.accessDeniedHandler(new CustomAccessDeniedHandler())) // 권한 핸들러 (403에러)
			.addFilterBefore(new JwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

		return http.build();

	}

	// 패스워드 암호화
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationManager authenticationManager
		(AuthenticationConfiguration authConfig) throws Exception {
		return authConfig.getAuthenticationManager();
	}

	private CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration corsConfig = new CorsConfiguration();

		corsConfig.setAllowedOriginPatterns(List.of("*"));
		corsConfig.setAllowedMethods(List.of(
			HttpMethod.GET.name(),
			HttpMethod.POST.name(),
			HttpMethod.PATCH.name(),
			HttpMethod.DELETE.name(),
			HttpMethod.OPTIONS.name()
		));
		corsConfig.setAllowedHeaders(List.of(
			HttpHeaders.AUTHORIZATION,
			HttpHeaders.CACHE_CONTROL,
			HttpHeaders.CONTENT_TYPE));

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", corsConfig);
		return source;
	}
}
