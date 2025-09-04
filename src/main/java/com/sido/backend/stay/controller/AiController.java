package com.sido.backend.stay.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sido.backend.stay.service.OpenAiService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@Tag(name = "OpenAI api 연결")
@RequestMapping("/api/admin/ai")
@PreAuthorize("hasRole('ROLE_ADMIN')")
@RequiredArgsConstructor
public class AiController {
	private final OpenAiService openAiService;

	@Value("${app.s3.publicBaseUrl}")
	private String publicBaseUrl;

	@Operation(description = "OpenAI api 이용한 이미지 설명 생성")
	@GetMapping("/description")
	public ResponseEntity<String> generateCaption(@RequestParam String s3Key) {
		String imageUrl = publicBaseUrl + '/' + s3Key;
		String caption = openAiService.generateCaption(imageUrl);
		return ResponseEntity.ok(caption);
	}
}