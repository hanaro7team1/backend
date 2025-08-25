package com.sido.backend.festival.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sido.backend.festival.dto.FestivalRequestDTO;
import com.sido.backend.festival.service.FestivalService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/festivals")
@Tag(name = "축제")
public class FestivalController {
	private final FestivalService service;

	public FestivalController(FestivalService service) {
		this.service = service;
	}

	@Operation(description = "조회")
	@GetMapping
	// PageResponseDTO<FestivalResponseDTO, Festival>
	public ResponseEntity<?> getFestivalList(
		@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "5") int listSize) {

		return ResponseEntity.ok(service.getFestivalList(page, listSize));
	}

	@Operation(description = "상세조회")
	@GetMapping("/{id}")
	public ResponseEntity<?> getServiceDetail(@PathVariable long id) {
		return ResponseEntity.ok(service.getServiceDetail(id));
	}

	@Operation(description = "등록")
	@PostMapping
	public ResponseEntity<?> addFestival(@RequestBody FestivalRequestDTO requestDTO) {
		return ResponseEntity.ok(service.addFestival(requestDTO));
	}

	@Operation(description = "수정")
	@PatchMapping
	public ResponseEntity<?> editFestival(@RequestBody FestivalRequestDTO requestDTO) {
		return ResponseEntity.ok(service.editFestival(requestDTO));
	}

	@Operation(description = "삭제")
	@DeleteMapping("/{id}")
	public long removeFestival(@PathVariable Long id) {
		service.removeFestival(id);
		return id;
	}
}
