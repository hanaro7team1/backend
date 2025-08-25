package com.sido.backend.stay.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sido.backend.stay.service.StayService;

@Controller
@RequestMapping("/api/stay")
public class StayController {
	private final StayService service;

	public StayController(StayService service) {
		this.service = service;
	}
}
