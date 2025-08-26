package com.sido.backend.stay.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sido.backend.stay.dto.StayDTO;
import com.sido.backend.stay.dto.StayRequestDTO;
import com.sido.backend.stay.service.StayService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/admin/stays")
@Tag(name = "사랑방")
public class StayAdminController {
	private final StayService service;

	public StayAdminController(StayService service) {
		this.service = service;
	}

	@Operation(description = "등록")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PostMapping
	public ResponseEntity<?> addStay(@AuthenticationPrincipal(expression = "memberId") Long memberId, @Valid @RequestBody StayRequestDTO requestDTO) {
		return ResponseEntity.ok(service.addStay(memberId, requestDTO));
	}

	@Operation(description = "수정")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PatchMapping({"/{id}"})
	public ResponseEntity<?> editStay(@PathVariable long id, @AuthenticationPrincipal(expression = "memberId") Long memberId, @Valid @RequestBody StayDTO stayDTO) {
		return ResponseEntity.ok(service.editStay(id, memberId, stayDTO));
	}
}
