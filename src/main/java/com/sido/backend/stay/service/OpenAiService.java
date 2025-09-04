package com.sido.backend.stay.service;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.sido.backend.stay.dto.OpenAiResponseDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OpenAiService {

	private final RestTemplate restTemplate = new RestTemplate();
	@Value("${openai.api.key}")
	private String apiKey;
	@Value("${openai.api.url}")
	private String apiUrl;

	public String generateCaption(String imageUrl) {
		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(apiKey);
		headers.setContentType(MediaType.APPLICATION_JSON);

		Map<String, Object> request = Map.of(
			"model", "gpt-4.1-mini",
			"input", List.of(
				Map.of("role", "user", "content", List.of(
					Map.of("type", "input_text", "text",
						"이 사진은 집에 남는 방을 빌려주는 귀촌 체험 서비스에 쓰일 거야 최대한 친절하고 두루뭉술하지 않게 설명 적어주면 좋겠어"),
					Map.of("type", "input_image", "image_url", imageUrl)
				))
			)
		);

		HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request, headers);
		ResponseEntity<OpenAiResponseDTO> response = restTemplate.postForEntity(apiUrl, entity,
			OpenAiResponseDTO.class);

		OpenAiResponseDTO body = Objects.requireNonNull(response.getBody(), "응답 바디 null");
		List<String> captions = body.texts();
		return String.join("\n", captions);
	}

}