package com.sido.backend.member.dto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MemberDTO extends User {
	private String loginId;
	private String password;
	private String role;

	private String name; // 일반 사용자 이름
	private String villageName; // 호스트 마을 이름

	public MemberDTO(String loginId, String password, String role, String name, String villageName) {
		super(loginId, password, List.of(new SimpleGrantedAuthority(role)));

		this.loginId = loginId;
		this.password = password;
		this.role = role;
		this.name = name;
		this.villageName = villageName;
	}

	public MemberDTO(String loginId, String password, String role, String displayName) {
		this(
			loginId,
			password,
			role,
			"ROLE_HOST".equals(role) ? null : displayName, // user, admin -> name
			"ROLE_HOST".equals(role) ? displayName : null // host -> villageName
		);
	}

	public Map<String, Object> getClaims() { // JWT Payload에 들어갈 claims
		Map<String, Object> map = new HashMap<>();
		map.put("loginId", loginId);
		// map.put("password", password); // claim에 password 넣지 X
		map.put("role", role);
		map.put("name", getDisplayName());

		return map;
	}

	public boolean isHost() {
		return "ROLE_HOST".equals(role);
	}

	public String getDisplayName() {
		return isHost() ? villageName : name;
	}
}
