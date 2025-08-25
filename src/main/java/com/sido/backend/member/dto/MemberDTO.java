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
	private Long memberId; // 컨트롤러에서 memberId 가져오도록 @AuthenticationPrincipal 쓰기 위해 추가
	private String loginId;
	private String password;
	private String role;

	private String name; // 일반 사용자 이름
	private String villageName; // 호스트 마을 이름

	public MemberDTO(Long memberId, String loginId, String password, String role, String name, String villageName) {
		super(loginId, password, List.of(new SimpleGrantedAuthority(role)));

		this.memberId = memberId;
		this.loginId = loginId;
		this.password = password;
		this.role = role;
		this.name = name;
		this.villageName = villageName;
	}

	public MemberDTO(Long memberId, String loginId, String password, String role, String displayName) {
		this(
			memberId,
			loginId,
			password,
			role,
			"ROLE_ADMIN".equals(role) ? null : displayName, // user -> name
			"ROLE_ADMIN".equals(role) ? displayName : null // admin -> villageName
		);
	}

	public Map<String, Object> getClaims() { // JWT Payload에 들어갈 claims
		Map<String, Object> map = new HashMap<>();
		map.put("memberId", memberId);
		map.put("loginId", loginId);
		// map.put("password", password); // claim에 password 넣지 X
		map.put("role", role);
		map.put("name", getDisplayName());

		return map;
	}

	public boolean isAdmin() {
		return "ROLE_ADMIN".equals(role);
	}

	public String getDisplayName() {
		return isAdmin() ? villageName : name;
	}
}
