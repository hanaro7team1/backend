package com.sido.backend.stay.controller;

import java.net.URL;
import java.time.Duration;
import java.util.Map;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sido.backend.stay.dto.PresignRequestDTO;
import com.sido.backend.stay.service.S3UploadService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin/upload")
@RequiredArgsConstructor
@Tag(name = "사랑방-S3이미지 업로드")
public class S3Controller {
	private final S3UploadService uploadService;

	@Operation(description = "presign 발급")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PostMapping("/presign")
	public Map<String, String> presign(@RequestBody PresignRequestDTO req) {
		// 예: domain=temp, ext=jpg -> temp/202508/{uuid}.jpg
		String ym = java.time.LocalDate.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyyMM"));
		String key = "%s/%s/%s.%s".formatted(req.getDomain(), ym, java.util.UUID.randomUUID(), req.getExtension());
		URL url = uploadService.createPresignedPutUrl(key, req.getContentType(), Duration.ofMinutes(10));
		return Map.of(
			"url", url.toString(),
			"key", key // 업로드 후 이 키를 DB에 저장
		);
	}
}