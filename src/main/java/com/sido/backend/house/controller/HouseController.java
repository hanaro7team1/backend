package com.sido.backend.house.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sido.backend.house.service.HouseService;

@Controller
@RequestMapping("/api/house")
public class HouseController {
	private final HouseService service;

	public HouseController(HouseService service) {
		this.service = service;
	}
}
