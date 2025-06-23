//package com.readup.server.user.presentation;
//
//import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.*;
//import static org.mockito.BDDMockito.*;
//import static org.springframework.restdocs.payload.JsonFieldType.*;
//import static org.springframework.restdocs.payload.PayloadDocumentation.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//import com.readup.server.user.application.UserLifecycleService;
//import org.junit.jupiter.api.Test;
//import org.mockito.MockedStatic;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.bean.override.mockito.MockitoBean;
//import org.springframework.test.web.servlet.MockMvc;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.readup.server.AbstractWebMvcTest;
//import com.readup.server.user.dto.CreateUserRequest;
//import com.readup.server.user.dto.UserResponse;
//import com.readup.server.user.generator.RandomNicknameGenerator;
//import com.readup.server.util.TermsTestUtils;
//import com.readup.server.util.WithCustomOAuth2User;
//
//@WebMvcTest(UserLifecycleController.class)
//@AutoConfigureMockMvc(addFilters = false)
//class UserRegisterControllerTest extends AbstractWebMvcTest {
//
//	@Autowired
//	private MockMvc mockMvc;
//
//	@Autowired
//	private ObjectMapper objectMapper;
//
//	@MockitoBean
//	private UserLifecycleService userLifecycleService;
//
//	@Test
//	void getRandomNickname() throws Exception {
//		// given
//		final String randomNickname = "지적인독서가";
//		final String uri = "/public/users/random-nickname";
//
//		// mocking
//		try (MockedStatic<RandomNicknameGenerator> mockedStatic = mockStatic(RandomNicknameGenerator.class)) {
//			mockedStatic.when(RandomNicknameGenerator::generate).thenReturn(randomNickname);
//
//			// when && then
//			mockMvc.perform(get(uri))
//				.andExpect(status().isOk())
//				.andExpect(jsonPath("$.data").value(randomNickname))
//
//				// docs
//				.andDo(document("user-get-random-nickname",
//					responseFields(
//						fieldWithPath("success").type(BOOLEAN).description("응답 성공 여부"),
//						fieldWithPath("data").type(STRING).description("랜덤 생성된 닉네임"),
//						fieldWithPath("message").type(STRING).description("성공 메시지")
//					)
//				));
//		}
//	}
//
//	@Test
//	@WithCustomOAuth2User
//	void testCreateUser() throws Exception {
//		CreateUserRequest createUserRequest = new CreateUserRequest(
//			TermsTestUtils.createUserTermsConsentList(),
//			"readUp"
//		);
//		UserResponse mockResponse = new UserResponse(1L, "지적인 도마뱀");
//
//		when(userLifecycleService.createUser(anyLong(), any(CreateUserRequest.class)))
//			.thenReturn(mockResponse);
//
//		mockMvc.perform(post("/private/users/signup")
//				.contentType(MediaType.APPLICATION_JSON)
//				.content(objectMapper.writeValueAsString(createUserRequest)))
//			.andExpect(status().isOk())
//			.andExpect(jsonPath("$.data.id").value(1L))
//			.andExpect(jsonPath("$.data.nickname").value("지적인 도마뱀"))
//			.andDo(document("user-create",
//				requestFields(
//					fieldWithPath("termsConsentRequestList").type(ARRAY).description("약관 동의 목록"),
//					fieldWithPath("termsConsentRequestList[].termsVersionId").type(NUMBER).description("약관 버전 ID"),
//					fieldWithPath("termsConsentRequestList[].code").type(STRING).description("약관 코드"),
//					fieldWithPath("termsConsentRequestList[].isConsent").type(BOOLEAN).description("동의 여부"),
//					fieldWithPath("nickname").type(STRING).description("사용자 닉네임")
//				),
//				responseFields(
//					fieldWithPath("success").type(BOOLEAN).description("응답 성공 여부"),
//					fieldWithPath("data.id").type(NUMBER).description("생성된 사용자 ID"),
//					fieldWithPath("data.nickname").type(STRING).description("사용자 닉네임"),
//					fieldWithPath("message").type(STRING).description("성공 메시지")
//				)
//			));
//	}
//}