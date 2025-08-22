package com.sido.backend.festival.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sido.backend.festival.dto.FestivalResponseDTO;
import com.sido.backend.festival.dto.FestivalResponseDetailDTO;
import com.sido.backend.festival.dto.FestivalRequestDTO;
import com.sido.backend.festival.service.FestivalService;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/festivals")
public class FestivalController {
	private final FestivalService service;

	public FestivalController(FestivalService service) {
		this.service = service;
	}

	@Tag(name = "전체 조회")
	@GetMapping
	public List<FestivalResponseDTO> getFestivalList(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "5") int listSize) {
		return service.getFestivalList(page, listSize);
	}

	@Tag(name = "상세조회")
	@GetMapping("/{id}")
	public FestivalResponseDetailDTO getServiceDetail(@PathVariable long id) {
		return service.getServiceDetail(id);
	}

	@Tag(name = "등록")
	@PostMapping
	public FestivalResponseDetailDTO addFestival(@RequestBody FestivalRequestDTO requestDTO) {
		return service.addFestival(requestDTO);
	}

	@Tag(name = "수정")
	@PatchMapping
	public FestivalResponseDetailDTO editFestival(@RequestBody FestivalRequestDTO requestDTO) {
		return service.editFestival(requestDTO);
	}

	@Tag(name = "삭제")
	@DeleteMapping("/{id}")
	public long removeFestival(@PathVariable Long id) {
		service.removeFestival(id);
		return id;
	}
}
