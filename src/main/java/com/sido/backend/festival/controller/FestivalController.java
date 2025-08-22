package com.sido.backend.festival.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sido.backend.festival.service.FestivalService;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/festival")
public class FestivalController {
	private final FestivalService service;

	public FestivalController(FestivalService service) {
		this.service = service;
	}



	@Tag(name = "축제 삭제")
	@DeleteMapping("/{id}")
	public long removeFestival(@PathVariable Long id) {
		service.removeFestival(id);
		return id;
	}
}
