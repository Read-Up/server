package com.readup.server.book.infrastructure.client.gemini;

import java.util.List;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.readup.server.book.application.client.ParseChapterClient;
import com.readup.server.book.application.client.vo.ChapterVO;
import com.readup.server.book.infrastructure.client.gemini.dto.GeminiRequest;
import com.readup.server.book.infrastructure.client.gemini.dto.GeminiResponse;
import com.readup.server.book.infrastructure.client.gemini.feign.GeminiFeignClient;

import io.jsonwebtoken.lang.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class GeminiClient implements ParseChapterClient {
	private final GeminiClientProperties geminiClientProperties;
	private final GeminiFeignClient geminiFeignClient;
	private final ObjectMapper objectMapper;

	public List<ChapterVO> parseRawChapter(String rawChapter) {

		if (Objects.isEmpty(rawChapter)) {
			return List.of();
		}

		GeminiRequest geminiRequest = GeminiRequest.builder()
			.systemInstruction(geminiClientProperties.getParseChapterPromptTemplate())
			.contents(rawChapter)
			.build();

		GeminiResponse geminiResponse = geminiFeignClient.getGeminiResponse(
			geminiClientProperties.getApiKey(),
			geminiRequest
		);

		try {
			return objectMapper.readValue(
				geminiResponse.getText().replace("```json", "").replace("```", "").trim(),
				new TypeReference<>() {
				}
			);
		} catch (Exception e) {
			log.error("Error parsing JSON response: {}", e.getMessage());
			return List.of();
		}
	}
}
