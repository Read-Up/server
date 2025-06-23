package com.readup.server.quiz.presentation;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.*;
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
import org.junit.jupiter.api.Nested;
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
import com.readup.server.quiz.application.dto.GetQuizSetResponse.GetQuizResponse;

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
		final LocalDateTime createdAt = LocalDateTime.of(2025, 4, 16, 10, 0, 0);

		final int totalQuizCount = 1;
		final int time = 2;
		final int estimatedTime = totalQuizCount * time;

		final Long quizId = 1L;
		final int quizSequence = 1;
		final String quizQuestion = "자바의 정수형 기본 타입 중 하나는 무엇인가요?";
		final String quizExplanation = "-2^31 ~ 2^31-1 의 범위를 갖습니다.";

		final Long quizOptionId1 = 1L;
		final int quizOptionSequence1 = 1;
		final String quizOptionContent1 = "int";
		final boolean quizOptionCorrect1 = true;
		final Long quizOptionId2 = 2L;
		final int quizOptionSequence2 = 2;
		final String quizOptionContent2 = "long";
		final boolean quizOptionCorrect2 = false;

		CreateQuizSetRequest request = new CreateQuizSetRequest(
			bookId, chapterId, List.of(new CreateQuizRequest(
			quizQuestion, quizExplanation, List.of(
			new CreateQuizOptionRequest(quizOptionContent1, quizOptionCorrect1),
			new CreateQuizOptionRequest(quizOptionContent2, quizOptionCorrect2))))
		);

		CreateQuizSetResponse response = new CreateQuizSetResponse(
			bookId, chapterId, quizSetId, estimatedTime, totalQuizCount, createdAt, List.of(
			new CreateQuizSetResponse.CreateQuizResponse(
				quizId, quizSequence, quizQuestion, quizExplanation, List.of(
				new CreateQuizOptionResponse(quizOptionId1, quizOptionSequence1, quizOptionContent1,
					quizOptionCorrect1),
				new CreateQuizOptionResponse(quizOptionId2, quizOptionSequence2, quizOptionContent2,
					quizOptionCorrect2))))
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
				jsonPath("$.data.estimatedTime").value(estimatedTime),
				jsonPath("$.data.quizResponseList[0].quizId").value(quizId),
				jsonPath("$.data.quizResponseList[0].sequence").value(quizSequence),
				jsonPath("$.data.quizResponseList[0].question").value(quizQuestion),
				jsonPath("$.data.quizResponseList[0].explanation").value(quizExplanation),
				jsonPath("$.data.quizResponseList[0].quizOptionResponseList[0].quizOptionId").value(quizOptionId1),
				jsonPath("$.data.quizResponseList[0].quizOptionResponseList[0].sequence").value(quizOptionSequence1),
				jsonPath("$.data.quizResponseList[0].quizOptionResponseList[0].content").value(quizOptionContent1),
				jsonPath("$.data.quizResponseList[0].quizOptionResponseList[0].isCorrect").value(quizOptionCorrect1),
				jsonPath("$.data.quizResponseList[0].quizOptionResponseList[1].quizOptionId").value(quizOptionId2),
				jsonPath("$.data.quizResponseList[0].quizOptionResponseList[1].sequence").value(quizOptionSequence2),
				jsonPath("$.data.quizResponseList[0].quizOptionResponseList[1].content").value(quizOptionContent2),
				jsonPath("$.data.quizResponseList[0].quizOptionResponseList[1].content").value(quizOptionContent2),
				jsonPath("$.data.quizResponseList[0].quizOptionResponseList[1].isCorrect").value(quizOptionCorrect2),
				jsonPath("$.message").value(DEFAULT_SUCCESS_MESSAGE))

			// docs
			.andDo(
				MockMvcRestDocumentationWrapper.document("create-quiz-set",
					resourceDetails().tag("QuizSet"),
					requestFields(
						fieldWithPath("bookId").type(NUMBER).description("책 ID"),
						fieldWithPath("chapterId").type(NUMBER).description("챕터 ID"),
						fieldWithPath("quizRequestList[].question").type(STRING).description("퀴즈 질문"),
						fieldWithPath("quizRequestList[].explanation").type(STRING).description("퀴즈 해설"),
						fieldWithPath("quizRequestList[].quizOptionRequestList[].content").type(STRING)
							.description("퀴즈 보기 내용"),
						fieldWithPath("quizRequestList[].quizOptionRequestList[].isCorrect").type(BOOLEAN)
							.description("퀴즈 보기 정답 여부")
					),
					responseFields(
						fieldWithPath("success").type(BOOLEAN).description("성공 여부"),
						fieldWithPath("data.bookId").type(NUMBER).description("책 ID"),
						fieldWithPath("data.chapterId").type(NUMBER).description("챕터 ID"),
						fieldWithPath("data.quizSetId").type(NUMBER).description("퀴즈 세트 ID"),
						fieldWithPath("data.estimatedTime").type(NUMBER).description("퀴즈 세트 예상 소요 시각"),
						fieldWithPath("data.totalQuizCount").type(NUMBER).description("퀴즈 세트 내 퀴즈 총 개수"),
						fieldWithPath("data.createdAt").type(STRING).description("퀴즈 세트 생성 일시"),
						fieldWithPath("data.quizResponseList[].quizId").type(NUMBER).description("퀴즈 ID"),
						fieldWithPath("data.quizResponseList[].sequence").type(NUMBER).description("퀴즈 순서"),
						fieldWithPath("data.quizResponseList[].question").type(STRING).description("퀴즈 질문"),
						fieldWithPath("data.quizResponseList[].explanation").type(STRING).description("퀴즈 해설"),
						fieldWithPath("data.quizResponseList[].quizOptionResponseList[].quizOptionId").type(NUMBER)
							.description("퀴즈 보기 ID"),
						fieldWithPath("data.quizResponseList[].quizOptionResponseList[].sequence").type(NUMBER)
							.description("퀴즈 보기 순서"),
						fieldWithPath("data.quizResponseList[].quizOptionResponseList[].content").type(STRING)
							.description("퀴즈 보기 내용"),
						fieldWithPath("data.quizResponseList[].quizOptionResponseList[].isCorrect").type(BOOLEAN)
							.description("퀴즈 보기 정답 여부"),
						fieldWithPath("message").type(STRING).description("성공 메시지")
					)
				)
			);
	}

	@Nested
	@DisplayName("퀴즈 세트 상세 조회")
	class GetQuizSetTest {

		private final String uri = "/private/quiz-sets/{quizSetId}";
		private final Long bookId = 1L;
		private final Long chapterId = 1L;
		private final Long quizSetId = 1L;
		private final LocalDateTime createdAt = LocalDateTime.of(2025, 4, 16, 10, 0);

		private final Long quizId = 2L;
		private final int quizSequence = 1;
		private final String quizQuestion = "자바의 정수형 기본 타입 중 하나는 무엇인가요?";

		private final Long quizOptionId1 = 1L;
		private final int quizOptionSequence1 = 1;
		private final String quizOptionContent1 = "int";
		private final Long quizOptionId2 = 2L;
		private final int quizOptionSequence2 = 2;
		private final String quizOptionContent2 = "long";

		private GetQuizSetResponse createMockResponse() {
			return new GetQuizSetResponse(
				bookId, chapterId, quizSetId, createdAt, List.of(
				new GetQuizResponse(quizId, quizSequence, quizQuestion, List.of(
					new GetQuizOptionResponse(quizOptionId1, quizOptionSequence1, quizOptionContent1),
					new GetQuizOptionResponse(quizOptionId2, quizOptionSequence2, quizOptionContent2)))));
		}

		@Test
		@DisplayName("퀴즈 세트 상세 조회")
		void getQuizSetSuccessTest() throws Exception {
			// given
			final Long lastQuizId = 1L;
			GetQuizSetResponse response = createMockResponse();

			// stubbing
			when(quizSetService.getQuizSet(quizSetId, lastQuizId)).thenReturn(response);

			// when && then
			mockMvc.perform(get(uri, quizSetId).param("lastQuizId", String.valueOf(lastQuizId))
					.contentType(APPLICATION_JSON))
				.andExpectAll(
					status().isOk(),
					jsonPath("$.success").value(true),
					jsonPath("$.data.bookId").value(bookId),
					jsonPath("$.data.chapterId").value(chapterId),
					jsonPath("$.data.quizSetId").value(quizSetId),
					jsonPath("$.data.quizResponseList[0].quizId").value(quizId),
					jsonPath("$.data.quizResponseList[0].sequence").value(quizSequence),
					jsonPath("$.data.quizResponseList[0].question").value(quizQuestion),
					jsonPath("$.data.quizResponseList[0].quizOptionResponseList[0].quizOptionId").value(quizOptionId1),
					jsonPath("$.data.quizResponseList[0].quizOptionResponseList[0].sequence").value(
						quizOptionSequence1),
					jsonPath("$.data.quizResponseList[0].quizOptionResponseList[0].content").value(quizOptionContent1),
					jsonPath("$.data.quizResponseList[0].quizOptionResponseList[1].quizOptionId").value(quizOptionId2),
					jsonPath("$.data.quizResponseList[0].quizOptionResponseList[1].sequence").value(
						quizOptionSequence2),
					jsonPath("$.data.quizResponseList[0].quizOptionResponseList[1].content").value(quizOptionContent2),
					jsonPath("$.message").value(DEFAULT_SUCCESS_MESSAGE)
				)

				// docs
				.andDo(
					MockMvcRestDocumentationWrapper.document("get-quiz-set",
						resourceDetails().tag("QuizSet"),
						pathParameters(parameterWithName("quizSetId").description("퀴즈 세트 ID")),
						queryParameters(parameterWithName("lastQuizId").description("마지막으로 푼 퀴즈 ID (default: 0)")),
						responseFields(
							fieldWithPath("success").type(BOOLEAN).description("성공 여부"),
							fieldWithPath("data.bookId").type(NUMBER).description("책 ID"),
							fieldWithPath("data.chapterId").type(NUMBER).description("챕터 ID"),
							fieldWithPath("data.quizSetId").type(NUMBER).description("퀴즈 세트 ID"),
							fieldWithPath("data.createdAt").type(STRING).description("퀴즈 세트 생성 일시"),
							fieldWithPath("data.quizResponseList[].quizId").type(NUMBER).description("퀴즈 ID"),
							fieldWithPath("data.quizResponseList[].sequence").type(NUMBER).description("퀴즈 순서"),
							fieldWithPath("data.quizResponseList[].question").type(STRING).description("퀴즈 질문"),
							fieldWithPath("data.quizResponseList[].quizOptionResponseList[].quizOptionId").type(NUMBER)
								.description("퀴즈 보기 ID"),
							fieldWithPath("data.quizResponseList[].quizOptionResponseList[].sequence").type(NUMBER)
								.description("퀴즈 보기 순서"),
							fieldWithPath("data.quizResponseList[].quizOptionResponseList[].content").type(STRING)
								.description("퀴즈 보기 내용"),
							fieldWithPath("message").type(STRING).description("성공 메시지")
						)
					)
				);
		}
	}
}