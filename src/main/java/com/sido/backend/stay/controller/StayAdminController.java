package com.sido.backend.stay.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sido.backend.stay.dto.StayCreateDTO;
import com.sido.backend.stay.dto.StayResponseDetailDTO;
import com.sido.backend.stay.dto.StayUpdateDTO;
import com.sido.backend.stay.service.StayService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/admin/stays")
@Tag(name = "사랑방-Admin")
public class StayAdminController {
	private final StayService service;

	public StayAdminController(StayService service) {
		this.service = service;
	}

	@Operation(description = "등록")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PostMapping
	public ResponseEntity<StayResponseDetailDTO> addStay(
		@AuthenticationPrincipal(expression = "memberId") Long memberId,
		@Valid @RequestBody StayCreateDTO stayCreateDTO) {
		return ResponseEntity.ok(service.addStay(memberId, stayCreateDTO));
	}

	@Operation(description = "수정")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PatchMapping({"/{stayId}"})
	public ResponseEntity<?> editStay(@PathVariable long id,
		@AuthenticationPrincipal(expression = "memberId") Long memberId, @Valid @RequestBody StayUpdateDTO stayDTO) {
		return ResponseEntity.ok(service.editStay(id, memberId, stayDTO));
	}

	@Operation(description = "삭제")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@DeleteMapping("/{stayId}")
	public ResponseEntity<Void> deleteStay(@PathVariable Long stayId) {
		service.deleteStay(stayId);
		return ResponseEntity.noContent().build();
	}
}