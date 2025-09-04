package com.sido.backend.stay.dto;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true) //json에 dto에 없는 필드 없어도 무시
public class OpenAiResponseDTO {
	private List<Output> output;

	//응답 text 리스트로 반환 (널이나 공백 제거)
	public List<String> texts() {
		List<String> result = new ArrayList<>();
		if (output == null)
			return result;

		for (Output o : output) {
			if (o == null || o.getContent() == null)
				continue;

			for (Content c : o.getContent()) {
				if (c == null)
					continue;
				// text는 content.type == "output_text"일 때만 의미가 있음
				String t = c.getText();
				if (t != null && !t.isBlank()) {
					result.add(t.trim());
				}
			}
		}
		return result;
	}

	@Getter
	@Setter
	@NoArgsConstructor
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Output {
		private String id;
		private String type; // "message"
		private String role; // "assistant"

		@JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
		private List<Content> content;
	}

	@Getter
	@Setter
	@NoArgsConstructor
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Content {
		private String type;
		private String text;
	}
}