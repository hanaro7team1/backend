package com.sido.backend.stay.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sido.backend.stay.dto.StayRequestDTO;
import com.sido.backend.stay.service.StayService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/stay")
@Tag(name = "사랑방")
public class StayController {
	private final StayService service;

	public StayController(StayService service) {
		this.service = service;
	}

	@Operation(description = "등록")
	@GetMapping
	public ResponseEntity<?> addStay(StayRequestDTO requestDTO) {
		return ResponseEntity.ok(service.addStay(requestDTO));
	}

	@Operation(description = "수정")
	@GetMapping
	public ResponseEntity<?> editStay(StayRequestDTO requestDTO) {
		return ResponseEntity.ok(service.editStay(requestDTO));
	}
}
