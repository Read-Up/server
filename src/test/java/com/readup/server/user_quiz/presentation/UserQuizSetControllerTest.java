package com.readup.server.user_quiz.presentation;

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
import java.util.Set;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.core.MethodParameter;
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
import com.readup.server.common.config.WebConfig;
import com.readup.server.common.resolver.CurrentAuthUserArgumentResolver;
import com.readup.server.common.resolver.CurrentSocialAccountArgumentResolver;
import com.readup.server.common.resolver.CurrentUserArgumentResolver;
import com.readup.server.user_quiz.application.UserQuizSetService;
import com.readup.server.user_quiz.application.dto.CompleteUserQuizSetResponse;
import com.readup.server.user_quiz.application.dto.GetUserQuizSetResponse;
import com.readup.server.user_quiz.application.dto.GetUserQuizSetResultResponse;
import com.readup.server.user_quiz.application.dto.StartUserQuizSetResponse;
import com.readup.server.user_quiz.application.dto.SubmitUserQuizRequest;
import com.readup.server.user_quiz.application.dto.SubmitUserQuizResponse;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(controllers = UserQuizSetController.class,
	excludeFilters = {
		@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = CurrentUserArgumentResolver.class),
		@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = CurrentSocialAccountArgumentResolver.class),
		@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = CurrentAuthUserArgumentResolver.class),
		@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebConfig.class)
	}
)
@Import(UserQuizSetControllerTest.AuthenticationPrincipalConfig.class)
class UserQuizSetControllerTest extends AbstractWebMvcTest {

	@MockitoBean
	private UserQuizSetService userQuizSetService;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	@DisplayName("사용자 퀴즈 세트 조회")
	void get_user_quiz_set() throws Exception {
		// given
		final String uri = "/private/quiz-sets/{quizSetId}/my-progress";
		final Long socialAccountId = 1L;
		final Long quizSetId = 1L;
		final Long userQuizSetId = 1L;
		final Long lastQuizId = 1L;
		final Boolean isEvaluated = true;
		final Boolean isDone = false;
		GetUserQuizSetResponse response = new GetUserQuizSetResponse(userQuizSetId, lastQuizId, isEvaluated, isDone);

		// stubbing
		when(userQuizSetService.getUserQuizSet(quizSetId, socialAccountId)).thenReturn(response);

		// when && then
		mockMvc.perform(get(uri, quizSetId).contentType(APPLICATION_JSON))

			.andExpectAll(
				status().isOk(),
				jsonPath("$.success").value(true),
				jsonPath("$.data.userQuizSetId").value(userQuizSetId),
				jsonPath("$.data.lastQuizId").value(lastQuizId),
				jsonPath("$.data.isEvaluated").value(isEvaluated),
				jsonPath("$.data.isDone").value(isDone),
				jsonPath("$.message").value(DEFAULT_SUCCESS_MESSAGE))

			// docs
			.andDo(
				MockMvcRestDocumentationWrapper.document("get-user-quiz-set",
					resourceDetails().tag("UserQuizSet"),
					pathParameters(parameterWithName("quizSetId").description("퀴즈 세트 ID")),

					responseFields(
						fieldWithPath("success").type(BOOLEAN).description("성공 여부"),
						fieldWithPath("data.userQuizSetId").type(NUMBER).description("사용자 퀴즈 세트 ID"),
						fieldWithPath("data.lastQuizId").type(NUMBER).description("마지막으로 푼 퀴즈 ID"),
						fieldWithPath("data.isEvaluated").type(BOOLEAN).description("평가 완료 여부"),
						fieldWithPath("data.isDone").type(BOOLEAN).description("퀴즈 세트 완료 여부"),
						fieldWithPath("message").type(STRING).description("성공 메시지")
					)
				)
			);
	}

	@Test
	@DisplayName("유저 퀴즈 세트 결과 조회")
	void get_user_quiz_set_result() throws Exception {
		// given
		final String uri = "/private/user-quiz-sets/{userQuizSetId}/result";
		final Long socialAccountId = 1L;
		final Long userQuizSetId = 1L;
		final int solvedQuizCount = 5;
		final int firstAttemptCorrectCount = 3;
		final int retryCorrectCount = 2;
		final boolean isAboveHalfCorrect = true;
		final GetUserQuizSetResultResponse response = new GetUserQuizSetResultResponse(
			solvedQuizCount, firstAttemptCorrectCount, retryCorrectCount, isAboveHalfCorrect);

		// stubbing
		when(userQuizSetService.getUserQuizSetResult(userQuizSetId, socialAccountId)).thenReturn(response);

		// when && then
		mockMvc.perform(get(uri, userQuizSetId)
				.contentType(APPLICATION_JSON))
			.andExpectAll(
				status().isOk(),
				jsonPath("$.success").value(true),
				jsonPath("$.data.solvedQuizCount").value(solvedQuizCount),
				jsonPath("$.data.firstAttemptCorrectCount").value(firstAttemptCorrectCount),
				jsonPath("$.data.retryCorrectCount").value(retryCorrectCount),
				jsonPath("$.data.isAboveHalfCorrect").value(isAboveHalfCorrect),
				jsonPath("$.message").value(DEFAULT_SUCCESS_MESSAGE))

			// docs
			.andDo(
				MockMvcRestDocumentationWrapper.document("get-user-quiz-set-result",
					resourceDetails().tag("UserQuizSet"),
					pathParameters(parameterWithName("userQuizSetId").description("유저 퀴즈 세트 ID")),
					responseFields(
						fieldWithPath("success").type(BOOLEAN).description("성공 여부"),
						fieldWithPath("data.solvedQuizCount").type(NUMBER).description("참여한 퀴즈 수"),
						fieldWithPath("data.firstAttemptCorrectCount").type(NUMBER).description("한 번에 마스터한 지식"),
						fieldWithPath("data.retryCorrectCount").type(NUMBER).description("재도전으로 강화된 지식"),
						fieldWithPath("data.isAboveHalfCorrect").type(BOOLEAN).description("한 번에 마스터한 지식 50% 이상 여부"),
						fieldWithPath("message").type(STRING).description("성공 메시지")
					)
				)
			);
	}

	@Test
	@DisplayName("유저 퀴즈 세트 시작")
	void start_user_quiz_set() throws Exception {
		// given
		final String uri = "/private/user-quiz-sets/{userQuizSetId}/start";
		final Long userQuizSetId = 1L;
		final Long socialAccountId = 1L;
		final Long quizSetId = 1L;
		final LocalDateTime startedAt = LocalDateTime.of(2025, 7, 19, 10, 30, 0, 0);
		final StartUserQuizSetResponse response = StartUserQuizSetResponse.of(userQuizSetId, quizSetId, startedAt);

		// stubbing
		when(userQuizSetService.startUserQuizSet(any(), any(), any()))
			.thenReturn(response);

		// when && then
		mockMvc.perform(post(uri, userQuizSetId)
				.contentType(APPLICATION_JSON))
			.andExpectAll(
				status().isOk(),
				jsonPath("$.success").value(true),
				jsonPath("$.data.userQuizSetId").value(userQuizSetId),
				jsonPath("$.data.quizSetId").value(socialAccountId),
				jsonPath("$.message").value(DEFAULT_SUCCESS_MESSAGE))

			// docs
			.andDo(
				MockMvcRestDocumentationWrapper.document("start-user-quiz-set",
					resourceDetails().tag("UserQuizSet"),
					pathParameters(parameterWithName("userQuizSetId").description("유저 퀴즈 세트 ID")),
					responseFields(
						fieldWithPath("success").type(BOOLEAN).description("성공 여부"),
						fieldWithPath("data.userQuizSetId").type(NUMBER).description("유저 퀴즈 세트 ID"),
						fieldWithPath("data.quizSetId").type(NUMBER).description("퀴즈 세트 ID"),
						fieldWithPath("data.startedAt").type(STRING).description("유저 퀴즈 세트 시작 시각"),
						fieldWithPath("message").type(STRING).description("성공 메시지")
					)
				)
			);
	}

	@Test
	@DisplayName("유저 퀴즈 세트 완료")
	void complete_user_quiz_set() throws Exception {
		// given
		final String uri = "/private/user-quiz-sets/{userQuizSetId}/complete";
		final Long userQuizSetId = 1L;
		final Long socialAccountId = 1L;
		final Long quizSetId = 1L;
		final LocalDateTime completedAt = LocalDateTime.of(2025, 7, 19, 10, 30, 0, 0);
		final CompleteUserQuizSetResponse response = CompleteUserQuizSetResponse.of(userQuizSetId, quizSetId, completedAt);

		// stubbing
		when(userQuizSetService.completeUserQuizSet(any(), any(), any()))
			.thenReturn(response);

		// when && then
		mockMvc.perform(post(uri, userQuizSetId)
				.contentType(APPLICATION_JSON))
			.andExpectAll(
				status().isOk(),
				jsonPath("$.success").value(true),
				jsonPath("$.data.userQuizSetId").value(userQuizSetId),
				jsonPath("$.data.quizSetId").value(socialAccountId),
				jsonPath("$.message").value(DEFAULT_SUCCESS_MESSAGE))

			// docs
			.andDo(
				MockMvcRestDocumentationWrapper.document("complete-user-quiz-set",
					resourceDetails().tag("UserQuizSet"),
					pathParameters(parameterWithName("userQuizSetId").description("유저 퀴즈 세트 ID")),
					responseFields(
						fieldWithPath("success").type(BOOLEAN).description("성공 여부"),
						fieldWithPath("data.userQuizSetId").type(NUMBER).description("유저 퀴즈 세트 ID"),
						fieldWithPath("data.quizSetId").type(NUMBER).description("퀴즈 세트 ID"),
						fieldWithPath("data.completedAt").type(STRING).description("유저 퀴즈 세트 완료 시각"),
						fieldWithPath("message").type(STRING).description("성공 메시지")
					)
				)
			);
	}

	@Test
	@DisplayName("사용자 퀴즈 답안 제출")
	void submit_user_quiz_answer() throws Exception {
		// given
		final String uri = "/private/quiz-sets/{quizSetId}/quizzes/{quizId}/answer";
		final Long socialAccountId = 1L;
		final Long quizSetId = 1L;
		final Long quizId = 1L;
		final Set<Long> selectedQuizOptionIds = Set.of(1L);
		final String explanation = "-2^31 ~ 2^31-1 의 범위를 갖습니다.";

		SubmitUserQuizRequest request = new SubmitUserQuizRequest(selectedQuizOptionIds);
		SubmitUserQuizResponse response = new SubmitUserQuizResponse(true, explanation);

		// stubbing
		when(userQuizSetService.submitUserQuizAnswer(quizSetId, quizId, request, socialAccountId)).thenReturn(response);

		// when && then
		mockMvc.perform(post(uri, quizSetId, quizId)
				.contentType(APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))

			.andExpectAll(
				status().isOk(),
				jsonPath("$.success").value(true),
				jsonPath("$.data.isCorrect").value(true),
				jsonPath("$.data.explanation").value(explanation),
				jsonPath("$.message").value(DEFAULT_SUCCESS_MESSAGE))

			// docs
			.andDo(
				MockMvcRestDocumentationWrapper.document("submit-user-quiz-answer",
					resourceDetails().tag("UserQuizSet"),
					pathParameters(
						parameterWithName("quizSetId").description("퀴즈 세트 ID"),
						parameterWithName("quizId").description("퀴즈 ID")
					),
					requestFields(
						fieldWithPath("selectedQuizOptionIds").type(ARRAY)
							.description("선택한 퀴즈 옵션 ID 리스트")
					),
					responseFields(
						fieldWithPath("success").type(BOOLEAN).description("성공 여부"),
						fieldWithPath("data.isCorrect").type(BOOLEAN).description("정답 여부"),
						fieldWithPath("data.explanation").type(STRING).description("정답 설명 (정답일 경우에만 제공 오답인 경우 null)"),
						fieldWithPath("message").type(STRING).description("성공 메시지")
					)
				)
			);
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