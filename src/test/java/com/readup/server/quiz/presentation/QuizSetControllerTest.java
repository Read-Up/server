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
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.core.MethodParameter;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.readup.server.AbstractWebMvcTest;
import com.readup.server.auth.dto.CustomOAuth2User;
import com.readup.server.quiz.application.QuizSetService;
import com.readup.server.quiz.application.dto.CreateQuizSetRequest;
import com.readup.server.quiz.application.dto.CreateQuizSetRequest.CreateQuizRequest;
import com.readup.server.quiz.application.dto.CreateQuizSetRequest.CreateQuizRequest.CreateQuizOptionRequest;
import com.readup.server.quiz.application.dto.CreateQuizSetResponse;
import com.readup.server.quiz.application.dto.CreateQuizSetResponse.CreateQuizOptionResponse;
import com.readup.server.quiz.application.dto.GetQuizExplanationResponse;
import com.readup.server.quiz.application.dto.GetQuizSetPageResponse;
import com.readup.server.quiz.application.dto.GetQuizSetResponse;
import com.readup.server.quiz.application.dto.GetQuizSetResponse.GetQuizOptionResponse;
import com.readup.server.quiz.application.dto.GetQuizSetResponse.GetQuizResponse;
import com.readup.server.quiz.application.dto.SliceResponse;

@Import(QuizSetControllerTest.AuthenticationPrincipalConfig.class)
@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(controllers = QuizSetController.class, excludeFilters = @Filter(
	type = FilterType.REGEX,
	pattern = "com.readup.server.common..*")
)
class QuizSetControllerTest extends AbstractWebMvcTest {

	@MockitoBean
	private QuizSetService quizSetService;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	@DisplayName("퀴즈 세트 추가")
	void create_quizSet_success() throws Exception {
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
				jsonPath("$.data.quizResponseList[0].quizOptionResponseList[0].sequence").value(
					quizOptionSequence1),
				jsonPath("$.data.quizResponseList[0].quizOptionResponseList[0].content").value(quizOptionContent1),
				jsonPath("$.data.quizResponseList[0].quizOptionResponseList[0].isCorrect").value(
					quizOptionCorrect1),
				jsonPath("$.data.quizResponseList[0].quizOptionResponseList[1].quizOptionId").value(quizOptionId2),
				jsonPath("$.data.quizResponseList[0].quizOptionResponseList[1].sequence").value(
					quizOptionSequence2),
				jsonPath("$.data.quizResponseList[0].quizOptionResponseList[1].content").value(quizOptionContent2),
				jsonPath("$.data.quizResponseList[0].quizOptionResponseList[1].content").value(quizOptionContent2),
				jsonPath("$.data.quizResponseList[0].quizOptionResponseList[1].isCorrect").value(
					quizOptionCorrect2),
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

	@DisplayName("퀴즈 세트 상세 조회")
	@Test
	void get_quizSet_success() throws Exception {
		// given
		final String uri = "/private/quiz-sets/{quizSetId}";
		final Long bookId = 1L;
		final Long chapterId = 1L;
		final Long quizSetId = 1L;
		final LocalDateTime createdAt = LocalDateTime.of(2025, 4, 16, 10, 0);

		final Long quizId = 2L;
		final int quizSequence = 1;
		final String quizQuestion = "자바의 정수형 기본 타입 중 하나는 무엇인가요?";

		final Long quizOptionId1 = 1L;
		final int quizOptionSequence1 = 1;
		final String quizOptionContent1 = "int";
		final Long quizOptionId2 = 2L;
		final int quizOptionSequence2 = 2;
		final String quizOptionContent2 = "long";
		final Long lastQuizId = 1L;
		final GetQuizSetResponse response = new GetQuizSetResponse(
			bookId, chapterId, quizSetId, createdAt, List.of(
			new GetQuizResponse(quizId, quizSequence, quizQuestion, List.of(
				new GetQuizOptionResponse(quizOptionId1, quizOptionSequence1, quizOptionContent1),
				new GetQuizOptionResponse(quizOptionId2, quizOptionSequence2, quizOptionContent2)))));

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
						fieldWithPath("message").type(STRING).description("성공 메시지"))
				)
			);
	}

	@Test
	@DisplayName("퀴즈 세트 전체 조회")
	void get_all_quiz_sets_success() throws Exception {
		// given
		final String uri = "/private/quiz-sets";
		final Long bookId = 1L;
		final Long chapterId = 1L;
		final String nickname = "testUser";
		final Long quizSetId = 1L;
		final int totalQuizCount = 5;
		final int participantCount = 10;
		final double likeAverage = 4.5;
		final double correctAnswerAverage = 85.5;
		final int estimatedTime = 10;
		final LocalDateTime createdAt = LocalDateTime.of(2025, 4, 16, 10, 0, 0);

		final GetQuizSetPageResponse quizSetResponse = new GetQuizSetPageResponse(
			nickname, quizSetId, totalQuizCount, participantCount, likeAverage,
			correctAnswerAverage, estimatedTime, createdAt
		);

		final SliceResponse<GetQuizSetPageResponse> response = new SliceResponse<>(
			List.of(quizSetResponse), 1, 0, 20, true, true, List.of(
			new SliceResponse.SortResponse("liveAverage", Sort.Direction.DESC))
		);

		// stubbing
		when(quizSetService.getAllQuizSets(eq(bookId), eq(chapterId), any())).thenReturn(response);

		// when && then
		mockMvc.perform(get(uri)
				.param("bookId", String.valueOf(bookId))
				.param("chapterId", String.valueOf(chapterId))
				.param("page", "0")
				.param("size", "20")
				.param("sort", "likeAverage")
				.contentType(APPLICATION_JSON))
			.andExpectAll(
				status().isOk(),
				jsonPath("$.success").value(true),
				jsonPath("$.data.content[0].nickname").value(nickname),
				jsonPath("$.data.content[0].quizSetId").value(quizSetId),
				jsonPath("$.data.content[0].totalQuizCount").value(totalQuizCount),
				jsonPath("$.data.content[0].participantCount").value(participantCount),
				jsonPath("$.data.content[0].likeAverage").value(likeAverage),
				jsonPath("$.data.content[0].correctAnswerAverage").value(correctAnswerAverage),
				jsonPath("$.data.content[0].estimatedTime").value(estimatedTime),
				jsonPath("$.data.contentSize").value(1),
				jsonPath("$.data.currentPage").value(0),
				jsonPath("$.data.pageSize").value(20),
				jsonPath("$.data.isFirst").value(true),
				jsonPath("$.data.isLast").value(true),
				jsonPath("$.message").value(DEFAULT_SUCCESS_MESSAGE)
			)

			// docs
			.andDo(
				MockMvcRestDocumentationWrapper.document("get-all-quiz-sets",
					resourceDetails().tag("QuizSet"),
					queryParameters(
						parameterWithName("bookId").description("책 ID"),
						parameterWithName("chapterId").description("챕터 ID"),
						parameterWithName("page").description("페이지 번호 (default: 0)").optional(),
						parameterWithName("size").description("페이지 크기 (default: 10)").optional(),
						parameterWithName("sort").description(
							"정렬 기준 (likeAverage(default DESC), correctAnswerAverage, createdAt)").optional()
					),
					responseFields(
						fieldWithPath("success").type(BOOLEAN).description("성공 여부"),
						fieldWithPath("data.content[].nickname").type(STRING).description("작성자 닉네임"),
						fieldWithPath("data.content[].quizSetId").type(NUMBER).description("퀴즈 세트 ID"),
						fieldWithPath("data.content[].totalQuizCount").type(NUMBER).description("총 퀴즈 개수"),
						fieldWithPath("data.content[].participantCount").type(NUMBER).description("참여자 수"),
						fieldWithPath("data.content[].likeAverage").type(NUMBER).description("평균 좋아요"),
						fieldWithPath("data.content[].correctAnswerAverage").type(NUMBER).description("평균 정답률"),
						fieldWithPath("data.content[].estimatedTime").type(NUMBER).description("예상 소요 시간"),
						fieldWithPath("data.content[].createdAt").type(STRING).description("생성 일시"),
						fieldWithPath("data.contentSize").type(NUMBER).description("현재 페이지 콘텐츠 수"),
						fieldWithPath("data.currentPage").type(NUMBER).description("현재 페이지 번호"),
						fieldWithPath("data.pageSize").type(NUMBER).description("요청 콘텐츠 수"),
						fieldWithPath("data.isFirst").type(BOOLEAN).description("첫 페이지 여부"),
						fieldWithPath("data.isLast").type(BOOLEAN).description("마지막 페이지 여부"),
						fieldWithPath("data.sortList[].field").type(STRING).description("정렬 필드명"),
						fieldWithPath("data.sortList[].direction").type(STRING).description("정렬 방향"),
						fieldWithPath("message").type(STRING).description("성공 메시지")
					)
				)
			);
	}

	@Test
	@DisplayName("내 퀴즈 세트 조회")
	void get_my_quiz_sets_success() throws Exception {
		// given
		final String uri = "/private/my/quiz-sets";
		final Long userId = 1L;
		final Long bookId = 1L;
		final Long chapterId = 1L;
		final String nickname = "testUser";
		final Long quizSetId = 1L;
		final int totalQuizCount = 5;
		final int participantCount = 10;
		final double likeAverage = 4.5;
		final double correctAnswerAverage = 85.5;
		final int estimatedTime = 10;
		final LocalDateTime createdAt = LocalDateTime.of(2025, 4, 16, 10, 0, 0);

		final GetQuizSetPageResponse quizSetResponse = new GetQuizSetPageResponse(
			nickname, quizSetId, totalQuizCount, participantCount, likeAverage,
			correctAnswerAverage, estimatedTime, createdAt
		);

		final SliceResponse<GetQuizSetPageResponse> response = new SliceResponse<>(
			List.of(quizSetResponse), 1, 0, 20, true, true, List.of(
			new SliceResponse.SortResponse("liveAverage", Sort.Direction.DESC))
		);

		// stubbing
		when(quizSetService.getMyQuizSets(eq(userId), eq(bookId), eq(chapterId), any())).thenReturn(response);

		// when && then
		mockMvc.perform(get(uri)
				.param("bookId", String.valueOf(bookId))
				.param("chapterId", String.valueOf(chapterId))
				.param("page", "0")
				.param("size", "20")
				.param("sort", "createdAt")
				.contentType(APPLICATION_JSON))
			.andExpectAll(
				status().isOk(),
				jsonPath("$.success").value(true),
				jsonPath("$.data.content[0].nickname").value(nickname),
				jsonPath("$.data.content[0].quizSetId").value(quizSetId),
				jsonPath("$.data.content[0].totalQuizCount").value(totalQuizCount),
				jsonPath("$.data.content[0].participantCount").value(participantCount),
				jsonPath("$.data.content[0].likeAverage").value(likeAverage),
				jsonPath("$.data.content[0].correctAnswerAverage").value(correctAnswerAverage),
				jsonPath("$.data.content[0].estimatedTime").value(estimatedTime),
				jsonPath("$.data.contentSize").value(1),
				jsonPath("$.data.currentPage").value(0),
				jsonPath("$.data.pageSize").value(20),
				jsonPath("$.data.isFirst").value(true),
				jsonPath("$.data.isLast").value(true),
				jsonPath("$.message").value(DEFAULT_SUCCESS_MESSAGE)
			)

			// docs
			.andDo(
				MockMvcRestDocumentationWrapper.document("get-my-quiz-sets",
					resourceDetails().tag("QuizSet"),
					queryParameters(
						parameterWithName("bookId").description("책 ID"),
						parameterWithName("chapterId").description("챕터 ID"),
						parameterWithName("page").description("페이지 번호 (default: 0)").optional(),
						parameterWithName("size").description("페이지 크기 (default: 10)").optional(),
						parameterWithName("sort").description(
							"정렬 기준 (createdAt(default DESC), correctAnswerAverage, likeAverage").optional()
					),
					responseFields(
						fieldWithPath("success").type(BOOLEAN).description("성공 여부"),
						fieldWithPath("data.content[].nickname").type(STRING).description("작성자 닉네임"),
						fieldWithPath("data.content[].quizSetId").type(NUMBER).description("퀴즈 세트 ID"),
						fieldWithPath("data.content[].totalQuizCount").type(NUMBER).description("총 퀴즈 개수"),
						fieldWithPath("data.content[].participantCount").type(NUMBER).description("참여자 수"),
						fieldWithPath("data.content[].likeAverage").type(NUMBER).description("평균 좋아요"),
						fieldWithPath("data.content[].correctAnswerAverage").type(NUMBER).description("평균 정답률"),
						fieldWithPath("data.content[].estimatedTime").type(NUMBER).description("예상 소요 시간"),
						fieldWithPath("data.content[].createdAt").type(STRING).description("생성 일시"),
						fieldWithPath("data.contentSize").type(NUMBER).description("현재 페이지 콘텐츠 수"),
						fieldWithPath("data.currentPage").type(NUMBER).description("현재 페이지 번호"),
						fieldWithPath("data.pageSize").type(NUMBER).description("요청 콘텐츠 수"),
						fieldWithPath("data.isFirst").type(BOOLEAN).description("첫 페이지 여부"),
						fieldWithPath("data.isLast").type(BOOLEAN).description("마지막 페이지 여부"),
						fieldWithPath("data.sortList[].field").type(STRING).description("정렬 필드명"),
						fieldWithPath("data.sortList[].direction").type(STRING).description("정렬 방향"),
						fieldWithPath("message").type(STRING).description("성공 메시지")
					)
				)
			);
	}

	@Test
	@DisplayName("참여한 퀴즈 세트 조회")
	void get_participating_quiz_sets_success() throws Exception {
		// given
		final String uri = "/private/participating/quiz-sets";
		final Long userId = 1L;
		final Long bookId = 1L;
		final Long chapterId = 1L;
		final String nickname = "testUser";
		final Long quizSetId = 1L;
		final int totalQuizCount = 5;
		final int participantCount = 10;
		final double likeAverage = 4.5;
		final double correctAnswerAverage = 85.5;
		final int estimatedTime = 10;
		final LocalDateTime createdAt = LocalDateTime.of(2025, 4, 16, 10, 0, 0);

		GetQuizSetPageResponse quizSetResponse = new GetQuizSetPageResponse(
			nickname, quizSetId, totalQuizCount, participantCount, likeAverage,
			correctAnswerAverage, estimatedTime, createdAt
		);

		final SliceResponse<GetQuizSetPageResponse> response = new SliceResponse<>(
			List.of(quizSetResponse), 1, 0, 20, true, true, List.of(
			new SliceResponse.SortResponse("liveAverage", Sort.Direction.DESC))
		);

		CustomOAuth2User mockUser = mock(CustomOAuth2User.class);
		when(mockUser.id()).thenReturn(userId);

		// stubbing
		when(quizSetService.getParticipatingQuizSets(eq(userId), eq(bookId), eq(chapterId), any())).thenReturn(
			response);

		// when && then
		mockMvc.perform(get(uri)
				.param("bookId", String.valueOf(bookId))
				.param("chapterId", String.valueOf(chapterId))
				.param("page", "0")
				.param("size", "20")
				.param("sort", "solvedAt")
				.contentType(APPLICATION_JSON))
			.andExpectAll(
				status().isOk(),
				jsonPath("$.success").value(true),
				jsonPath("$.data.content[0].nickname").value(nickname),
				jsonPath("$.data.content[0].quizSetId").value(quizSetId),
				jsonPath("$.data.content[0].totalQuizCount").value(totalQuizCount),
				jsonPath("$.data.content[0].participantCount").value(participantCount),
				jsonPath("$.data.content[0].likeAverage").value(likeAverage),
				jsonPath("$.data.content[0].correctAnswerAverage").value(correctAnswerAverage),
				jsonPath("$.data.content[0].estimatedTime").value(estimatedTime),
				jsonPath("$.data.contentSize").value(1),
				jsonPath("$.data.currentPage").value(0),
				jsonPath("$.data.pageSize").value(20),
				jsonPath("$.data.isFirst").value(true),
				jsonPath("$.data.isLast").value(true),
				jsonPath("$.message").value(DEFAULT_SUCCESS_MESSAGE)
			)

			// docs
			.andDo(
				MockMvcRestDocumentationWrapper.document("get-participating-quiz-sets",
					resourceDetails().tag("QuizSet"),
					queryParameters(
						parameterWithName("bookId").description("책 ID)"),
						parameterWithName("chapterId").description("챕터 ID"),
						parameterWithName("page").description("페이지 번호 (default: 0)").optional(),
						parameterWithName("size").description("페이지 크기 (default: 10)").optional(),
						parameterWithName("sort").description(
							"정렬 기준 (solvedAt(default DESC), createdAt, correctAnswerAverage, likeAverage)").optional()
					),
					responseFields(
						fieldWithPath("success").type(BOOLEAN).description("성공 여부"),
						fieldWithPath("data.content[].nickname").type(STRING).description("작성자 닉네임"),
						fieldWithPath("data.content[].quizSetId").type(NUMBER).description("퀴즈 세트 ID"),
						fieldWithPath("data.content[].totalQuizCount").type(NUMBER).description("총 퀴즈 개수"),
						fieldWithPath("data.content[].participantCount").type(NUMBER).description("참여자 수"),
						fieldWithPath("data.content[].likeAverage").type(NUMBER).description("평균 좋아요"),
						fieldWithPath("data.content[].correctAnswerAverage").type(NUMBER).description("평균 정답률"),
						fieldWithPath("data.content[].estimatedTime").type(NUMBER).description("예상 소요 시간"),
						fieldWithPath("data.content[].createdAt").type(STRING).description("생성 일시"),
						fieldWithPath("data.contentSize").type(NUMBER).description("현재 페이지 콘텐츠 수"),
						fieldWithPath("data.currentPage").type(NUMBER).description("현재 페이지 번호"),
						fieldWithPath("data.pageSize").type(NUMBER).description("요청 콘텐츠 수"),
						fieldWithPath("data.isFirst").type(BOOLEAN).description("첫 페이지 여부"),
						fieldWithPath("data.isLast").type(BOOLEAN).description("마지막 페이지 여부"),
						fieldWithPath("data.sortList[].field").type(STRING).description("정렬 필드명"),
						fieldWithPath("data.sortList[].direction").type(STRING).description("정렬 방향"),
						fieldWithPath("message").type(STRING).description("성공 메시지")
					)
				)
			);
	}

	@Test
	@DisplayName("퀴즈 해설 조회")
	void get_quiz_explanation_success() throws Exception {
		//given
		final String uri = "/private/quizzes/{quizId}/explanation";
		final Long quizId = 1L;
		final String explanation = "-2^31 ~ 2^31-1 의 범위를 갖습니다.";
		final GetQuizExplanationResponse response = new GetQuizExplanationResponse(quizId, explanation);

		// stubbing
		when(quizSetService.getQuizExplanation(quizId)).thenReturn(response);

		// when && then
		mockMvc.perform(get(uri, quizId).contentType(APPLICATION_JSON))
			.andExpectAll(
				status().isOk(),
				jsonPath("$.success").value(true),
				jsonPath("$.data.quizId").value(quizId),
				jsonPath("$.data.explanation").value(explanation),
				jsonPath("$.message").value(DEFAULT_SUCCESS_MESSAGE)
			)

			// docs
			.andDo(
				MockMvcRestDocumentationWrapper.document("get-quiz-set",
					resourceDetails().tag("QuizSet"),
					pathParameters(parameterWithName("quizId").description("퀴즈 ID")),
					responseFields(
						fieldWithPath("success").type(BOOLEAN).description("성공 여부"),
						fieldWithPath("data.quizId").type(NUMBER).description("퀴즈 ID"),
						fieldWithPath("data.explanation").type(STRING).description("해설"),
						fieldWithPath("message").type(STRING).description("성공 메시지"))
				));

	}

	@TestConfiguration
	static class AuthenticationPrincipalConfig implements WebMvcConfigurer {

		@Override
		public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
			resolvers.add(customOAuth2UserArgumentResolver());
		}

		@Bean
		public HandlerMethodArgumentResolver customOAuth2UserArgumentResolver() {
			return new HandlerMethodArgumentResolver() {
				@Override
				public boolean supportsParameter(MethodParameter parameter) {
					return parameter.getParameterAnnotation(AuthenticationPrincipal.class) != null;
				}

				@Override
				public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
					NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
					return new CustomOAuth2User(1L, "test@example.com", null,
						List.of(new SimpleGrantedAuthority("ROLE_USER")));
				}
			};
		}
	}
}