package com.readup.server.quiz.presentation;

import static com.readup.server.common.dto.ApiResponse.*;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.readup.server.quiz.application.QuizService;
import com.readup.server.quiz.application.dto.CreateQuizListRequest;
import com.readup.server.quiz.application.dto.CreateQuizListRequest.CreateQuizRequest;
import com.readup.server.quiz.application.dto.CreateQuizListRequest.CreateQuizRequest.CreateQuizOptionRequest;
import com.readup.server.quiz.application.dto.CreateQuizListResponse;
import com.readup.server.quiz.application.dto.CreateQuizListResponse.CreateQuizOptionResponse;

@AutoConfigureRestDocs
@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(QuizController.class)
@ExtendWith(RestDocumentationExtension.class)
class QuizControllerTest {

	@MockitoBean
	private QuizService quizService;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	@DisplayName("퀴즈 추가 요청")
	void createQuizSuccessTest() throws Exception {
		// given
		final String uri = "/private/quizzes";
		final Long bookId = 1L;
		final Long chapterId = 1L;
		final String question = "자바의 정수형 기본 타입 중 하나는 무엇인가요?";
		final String explanation = "-2^31 ~ 2^31-1 의 범위를 갖습니다.";

		final int optionNumber1 = 1;
		final String optionContent1 = "int";
		final boolean optionCorrect1 = true;

		final int optionNumber2 = 2;
		final String optionContent2 = "long";
		final boolean optionCorrect2 = false;

		LocalDateTime createAt = LocalDateTime.of(2025, 4, 16, 10, 0);

		// given
		CreateQuizListRequest request = new CreateQuizListRequest(
			bookId, chapterId, List.of(new CreateQuizRequest(
			question, explanation, List.of(
			new CreateQuizOptionRequest(optionNumber1, optionContent1, optionCorrect1),
			new CreateQuizOptionRequest(optionNumber2, optionContent2, optionCorrect2)))));

		CreateQuizListResponse response = new CreateQuizListResponse(
			bookId, chapterId, List.of(
			new CreateQuizListResponse.CreateQuizResponse(
				question, explanation, List.of(
				new CreateQuizOptionResponse(optionNumber1, optionContent1, optionCorrect1),
				new CreateQuizOptionResponse(optionNumber2, optionContent2, optionCorrect2)),
				createAt)));

		when(quizService.createQuiz(any())).thenReturn(response);

		// when && then
		mockMvc.perform(post(uri)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))

			.andExpectAll(
				status().isOk(),
				jsonPath("$.success").value(true),
				jsonPath("$.data.bookId").value(bookId),
				jsonPath("$.data.chapterId").value(chapterId),
				jsonPath("$.data.quizResponseList[0].question").value(question),
				jsonPath("$.data.quizResponseList[0].explanation").value(explanation),
				jsonPath("$.data.quizResponseList[0].quizOptionResponseList[0].number").value(optionNumber1),
				jsonPath("$.data.quizResponseList[0].quizOptionResponseList[0].content").value(optionContent1),
				jsonPath("$.data.quizResponseList[0].quizOptionResponseList[0].isCorrect").value(optionCorrect1),
				jsonPath("$.data.quizResponseList[0].quizOptionResponseList[1].number").value(optionNumber2),
				jsonPath("$.data.quizResponseList[0].quizOptionResponseList[1].content").value(optionContent2),
				jsonPath("$.data.quizResponseList[0].quizOptionResponseList[1].isCorrect").value(optionCorrect2),
				jsonPath("$.message").value(DEFAULT_SUCCESS_MESSAGE))

			// docs
			.andDo(
				MockMvcRestDocumentationWrapper.document("quiz-create",
					requestFields(
						fieldWithPath("bookId").type(NUMBER).description("책 ID"),
						fieldWithPath("chapterId").type(NUMBER).description("챕터 ID"),
						fieldWithPath("quizRequestList[].question").type(STRING).description("퀴즈 질문"),
						fieldWithPath("quizRequestList[].explanation").type(STRING).description("퀴즈 해설"),
						fieldWithPath("quizRequestList[].quizOptionRequestList[].number").type(NUMBER)
							.description("보기 번호"),
						fieldWithPath("quizRequestList[].quizOptionRequestList[].content").type(STRING)
							.description("보기 내용"),
						fieldWithPath("quizRequestList[].quizOptionRequestList[].isCorrect").type(BOOLEAN)
							.description("보기 정답 여부")
					),
					responseFields(
						fieldWithPath("success").type(BOOLEAN).description("성공 여부"),
						fieldWithPath("data.bookId").type(NUMBER).description("책 ID"),
						fieldWithPath("data.chapterId").type(NUMBER).description("챕터 ID"),
						fieldWithPath("data.quizResponseList[].question").type(STRING).description("퀴즈 질문"),
						fieldWithPath("data.quizResponseList[].explanation").type(STRING).description("퀴즈 해설"),
						fieldWithPath("data.quizResponseList[].quizOptionResponseList[].number").type(NUMBER)
							.description("보기 번호"),
						fieldWithPath("data.quizResponseList[].quizOptionResponseList[].content").type(STRING)
							.description("보기 내용"),
						fieldWithPath("data.quizResponseList[].quizOptionResponseList[].isCorrect").type(BOOLEAN)
							.description("보기 정답 여부"),
						fieldWithPath("data.quizResponseList[].createAt").type(STRING).description("퀴즈 생성 일시"),
						fieldWithPath("message").type(STRING).description("성공 메시지")
					)
				)
			);
	}
}