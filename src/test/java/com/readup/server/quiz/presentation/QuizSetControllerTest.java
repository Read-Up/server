package com.readup.server.quiz.presentation;

import static com.readup.server.common.dto.ApiResponse.*;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.readup.server.AbstractWebMvcTest;
import com.readup.server.quiz.application.QuizSetService;
import com.readup.server.quiz.application.dto.CreateQuizSetRequest;
import com.readup.server.quiz.application.dto.CreateQuizSetRequest.CreateQuizRequest;
import com.readup.server.quiz.application.dto.CreateQuizSetRequest.CreateQuizRequest.CreateQuizOptionRequest;
import com.readup.server.quiz.application.dto.CreateQuizSetResponse;
import com.readup.server.quiz.application.dto.CreateQuizSetResponse.CreateQuizOptionResponse;
import com.readup.server.quiz.application.dto.GetQuizSetResponse;
import com.readup.server.quiz.application.dto.GetQuizSetResponse.GetQuizOptionResponse;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(QuizSetController.class)
class QuizSetControllerTest extends AbstractWebMvcTest {

	@MockitoBean
	private QuizSetService quizSetService;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	@DisplayName("퀴즈 세트 추가")
	void createQuizSetSuccessTest() throws Exception {
		// given
		final String uri = "/private/quiz-sets";
		final Long bookId = 1L;
		final Long chapterId = 1L;
		final Long quizSetId = 1L;
		final LocalDateTime createAt = LocalDateTime.of(2025, 4, 16, 10, 0);

		final String question = "자바의 정수형 기본 타입 중 하나는 무엇인가요?";
		final String explanation = "-2^31 ~ 2^31-1 의 범위를 갖습니다.";

		final String optionContent1 = "int";
		final boolean optionCorrect1 = true;
		final String optionContent2 = "long";
		final boolean optionCorrect2 = false;

		CreateQuizSetRequest request = new CreateQuizSetRequest(
			bookId, chapterId, List.of(new CreateQuizRequest(
			question, explanation, List.of(
			new CreateQuizOptionRequest(optionContent1, optionCorrect1),
			new CreateQuizOptionRequest(optionContent2, optionCorrect2))))
		);

		CreateQuizSetResponse response = new CreateQuizSetResponse(
			bookId, chapterId, quizSetId, createAt, List.of(
			new CreateQuizSetResponse.CreateQuizResponse(
				question, explanation, List.of(
				new CreateQuizOptionResponse(optionContent1, optionCorrect1),
				new CreateQuizOptionResponse(optionContent2, optionCorrect2))))
		);

		// stubbing
		when(quizSetService.createQuizSet(any())).thenReturn(response);

		// when && then
		mockMvc.perform(post(uri)
				.contentType(APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))

			.andExpectAll(
				status().isOk(),
				jsonPath("$.success").value(true),
				jsonPath("$.data.bookId").value(bookId),
				jsonPath("$.data.chapterId").value(chapterId),
				jsonPath("$.data.quizSetId").value(quizSetId),
				jsonPath("$.data.quizResponseList[0].question").value(question),
				jsonPath("$.data.quizResponseList[0].explanation").value(explanation),
				jsonPath("$.data.quizResponseList[0].quizOptionResponseList[0].content").value(optionContent1),
				jsonPath("$.data.quizResponseList[0].quizOptionResponseList[0].isCorrect").value(optionCorrect1),
				jsonPath("$.data.quizResponseList[0].quizOptionResponseList[1].content").value(optionContent2),
				jsonPath("$.data.quizResponseList[0].quizOptionResponseList[1].isCorrect").value(optionCorrect2),
				jsonPath("$.message").value(DEFAULT_SUCCESS_MESSAGE))

			// docs
			.andDo(
				MockMvcRestDocumentationWrapper.document("create-quiz-set",
					requestFields(
						fieldWithPath("bookId").type(NUMBER).description("책 ID"),
						fieldWithPath("chapterId").type(NUMBER).description("챕터 ID"),
						fieldWithPath("quizRequestList[].question").type(STRING).description("퀴즈 질문"),
						fieldWithPath("quizRequestList[].explanation").type(STRING).description("퀴즈 해설"),
						fieldWithPath("quizRequestList[].quizOptionRequestList[].content").type(STRING)
							.description("보기 내용"),
						fieldWithPath("quizRequestList[].quizOptionRequestList[].isCorrect").type(BOOLEAN)
							.description("보기 정답 여부")
					),
					responseFields(
						fieldWithPath("success").type(BOOLEAN).description("성공 여부"),
						fieldWithPath("data.bookId").type(NUMBER).description("책 ID"),
						fieldWithPath("data.chapterId").type(NUMBER).description("챕터 ID"),
						fieldWithPath("data.quizSetId").type(NUMBER).description("퀴즈 세트 ID"),
						fieldWithPath("data.createAt").type(STRING).description("퀴즈 세트 생성 일시"),
						fieldWithPath("data.quizResponseList[].question").type(STRING).description("퀴즈 질문"),
						fieldWithPath("data.quizResponseList[].explanation").type(STRING).description("퀴즈 해설"),
						fieldWithPath("data.quizResponseList[].quizOptionResponseList[].content").type(STRING)
							.description("보기 내용"),
						fieldWithPath("data.quizResponseList[].quizOptionResponseList[].isCorrect").type(BOOLEAN)
							.description("보기 정답 여부"),
						fieldWithPath("message").type(STRING).description("성공 메시지")
					)
				)
			);
	}

	@Test
	@DisplayName("퀴즈 세트 상세 조회 성공")
	void getQuizSetSuccessTest() throws Exception {
		// given
		final String uri = "/private/quiz-sets/{quizSetId}";
		final Long bookId = 1L;
		final Long chapterId = 1L;
		final Long quizSetId = 1L;

		final String question = "자바의 정수형 기본 타입 중 하나는 무엇인가요?";
		final String optionContent1 = "int";
		final String optionContent2 = "long";

		GetQuizSetResponse response = new GetQuizSetResponse(
			bookId, chapterId, quizSetId, List.of(
			new GetQuizSetResponse.GetQuizResponse(
				question, List.of(
				new GetQuizOptionResponse(optionContent1),
				new GetQuizOptionResponse(optionContent2)))));

		// stubbing
		when(quizSetService.getQuizSet(bookId, chapterId, quizSetId)).thenReturn(response);

		// when && then
		mockMvc.perform(get(uri, quizSetId)
				.param("bookId", bookId.toString())
				.param("chapterId", chapterId.toString())
				.contentType(APPLICATION_JSON))
			.andExpectAll(
				status().isOk(),
				jsonPath("$.success").value(true),
				jsonPath("$.data.bookId").value(bookId),
				jsonPath("$.data.chapterId").value(chapterId),
				jsonPath("$.data.quizSetId").value(quizSetId),
				jsonPath("$.data.quizResponseList[0].question").value(question),
				jsonPath("$.data.quizResponseList[0].quizOptionResponseList[0].content").value(optionContent1),
				jsonPath("$.data.quizResponseList[0].quizOptionResponseList[1].content").value(optionContent2),
				jsonPath("$.message").value(DEFAULT_SUCCESS_MESSAGE)
			)
			// docs
			.andDo(
				MockMvcRestDocumentationWrapper.document("get-quiz-set",
					queryParameters(
						parameterWithName("bookId").description("책 ID"),
						parameterWithName("chapterId").description("챕터 ID")
					),
					pathParameters(
						parameterWithName("quizSetId").description("퀴즈 세트 ID")
					),
					responseFields(
						fieldWithPath("success").type(BOOLEAN).description("성공 여부"),
						fieldWithPath("data.bookId").type(NUMBER).description("책 ID"),
						fieldWithPath("data.chapterId").type(NUMBER).description("챕터 ID"),
						fieldWithPath("data.quizSetId").type(NUMBER).description("퀴즈 세트 ID"),
						fieldWithPath("data.quizResponseList[].question").type(STRING).description("퀴즈 질문"),
						fieldWithPath("data.quizResponseList[].quizOptionResponseList[].content").type(STRING)
							.description("보기 내용"),
						fieldWithPath("message").type(STRING).description("성공 메시지")
					)
				)
			);
	}
}