package com.readup.server.book.infrastructure.client.gemini;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.readup.server.book.application.client.vo.ChapterVO;
import com.readup.server.book.infrastructure.client.gemini.dto.GeminiResponse;
import com.readup.server.book.infrastructure.client.gemini.feign.GeminiFeignClient;

@ExtendWith(MockitoExtension.class)
class GeminiClientTest {

	@InjectMocks
	private GeminiClient geminiClient;

	@Mock
	private GeminiClientProperties geminiClientProperties;

	@Mock
	private GeminiFeignClient geminiFeignClient;

	@Mock
	private ObjectMapper objectMapper;

	@Nested
	@DisplayName("parseRawChapter 메소드 테스트")
	class ParseRawChapter {

		@Test
		@DisplayName("빈 문자열 입력 시 빈 리스트 반환")
		void shouldReturnEmptyListWhenInputIsEmpty() {
			List<ChapterVO> result = geminiClient.parseRawChapter("");
			assertTrue(result.isEmpty());
		}

		@Test
		@DisplayName("정상 응답일 경우 챕터 리스트 반환")
		void shouldReturnChapterListWhenValidJson() throws Exception {
			// given
			String rawChapter = "<p>1장 시작</p>";
			String dummyJson = "```json\n[{\"chapterNumber\":1, \"name\":\"1장 시작\"}]\n```";
			GeminiResponse geminiResponse = new GeminiResponse(
				List.of(
					new GeminiResponse.Candidate(
						new GeminiResponse.Content(List.of(new GeminiResponse.Part(dummyJson)))
					)
				),
				"model",
				"STOP",
				0
			);

			ChapterVO chapterVO = ChapterVO.builder()
				.chapterNumber(1)
				.name("1장 시작")
				.build();

			given(geminiClientProperties.getParseChapterPromptTemplate()).willReturn("Prompt");
			given(geminiClientProperties.getApiKey()).willReturn("fake-api-key");
			given(geminiFeignClient.getGeminiResponse(any(), any())).willReturn(geminiResponse);
			given(objectMapper.readValue(anyString(), any(TypeReference.class)))
				.willReturn(List.of(chapterVO));

			// when
			List<ChapterVO> result = geminiClient.parseRawChapter(rawChapter);

			// then
			assertEquals(1, result.size());
			assertEquals(1, result.getFirst().chapterNumber());
			assertEquals("1장 시작", result.getFirst().name());
		}

		@Test
		@DisplayName("JSON 파싱 예외 발생 시 빈 리스트 반환")
		void shouldReturnEmptyListWhenJsonParseFails() throws Exception {
			// given
			String rawChapter = "<p>1장 시작</p>";
			String invalidJson = "```json\nINVALID_JSON\n```";

			GeminiResponse geminiResponse = new GeminiResponse(
				List.of(
					new GeminiResponse.Candidate(
						new GeminiResponse.Content(List.of(new GeminiResponse.Part(invalidJson)))
					)
				),
				"model",
				"STOP",
				0
			);

			given(geminiClientProperties.getParseChapterPromptTemplate()).willReturn("Prompt");
			given(geminiClientProperties.getApiKey()).willReturn("api-key");
			given(geminiFeignClient.getGeminiResponse(any(), any())).willReturn(geminiResponse);
			given(objectMapper.readValue(anyString(), any(TypeReference.class)))
				.willThrow(new RuntimeException("JSON parsing error"));

			// when
			List<ChapterVO> result = geminiClient.parseRawChapter(rawChapter);

			// then
			assertTrue(result.isEmpty());
		}
	}
}