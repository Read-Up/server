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

import java.util.List;
import java.util.Set;

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
import com.readup.server.user_quiz.application.UserQuizSetService;
import com.readup.server.user_quiz.application.dto.GetUserQuizSetResponse;
import com.readup.server.user_quiz.application.dto.SubmitUserQuizRequest;
import com.readup.server.user_quiz.application.dto.SubmitUserQuizResponse;

@Import(UserQuizSetControllerTest.AuthenticationPrincipalConfig.class)
@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(controllers = UserQuizSetController.class, excludeFilters = @Filter(
	type = FilterType.REGEX,
	pattern = "com.readup.server.common..*")
)
class UserQuizSetControllerTest extends AbstractWebMvcTest {

	@MockitoBean
	private UserQuizSetService userQuizSetService;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	@DisplayName("사용자 퀴즈 세트 조회")
	void getUserQuizSetSuccessTest() throws Exception {
		// given
		final String uri = "/private/quiz-sets/{quizSetId}/my-progress";
		final Long quizSetId = 1L;
		final Long userQuizSetId = 1L;
		final Long lastQuizId = 1L;
		final Boolean isEvaluated = true;
		GetUserQuizSetResponse response = new GetUserQuizSetResponse(userQuizSetId, lastQuizId, isEvaluated);

		// stubbing
		when(userQuizSetService.getUserQuizSet(any(), any())).thenReturn(response);

		// when && then
		mockMvc.perform(get(uri, quizSetId).contentType(APPLICATION_JSON))

			.andExpectAll(
				status().isOk(),
				jsonPath("$.success").value(true),
				jsonPath("$.data.userQuizSetId").value(userQuizSetId),
				jsonPath("$.data.lastQuizId").value(lastQuizId),
				jsonPath("$.data.isEvaluated").value(isEvaluated),
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
						fieldWithPath("message").type(STRING).description("성공 메시지")
					)
				)
			);
	}

	@Test
	@DisplayName("사용자 퀴즈 답안 제출")
	void submitUserQuizAnswerCorrectTest() throws Exception {
		// given
		final String uri = "/private/quiz-sets/{quizSetId}/quizzes/{quizId}/answer";
		final Long quizSetId = 1L;
		final Long quizId = 1L;
		final Set<Long> selectedQuizOptionIds = Set.of(1L);
		final String explanation = "-2^31 ~ 2^31-1 의 범위를 갖습니다.";

		SubmitUserQuizRequest request = new SubmitUserQuizRequest(selectedQuizOptionIds);
		SubmitUserQuizResponse response = new SubmitUserQuizResponse(true, explanation);

		// stubbing
		when(userQuizSetService.submitUserQuizAnswer(any(), any(), any(), any())).thenReturn(response);

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