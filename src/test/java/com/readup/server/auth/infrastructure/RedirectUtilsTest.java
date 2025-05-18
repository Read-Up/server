package com.readup.server.auth.infrastructure;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

class RedirectUtilsTest {

	private static final String BASE_URI = "http://localhost:3001";
	private static final String REDIRECT_URI_ATTRIBUTE = "redirect";
	private static final String TEST_REDIRECT_URI = "/login/success";

	@Test
	@DisplayName("세션이 null일때 /반환")
	void testGetRedirectUri_WithNullSession_ShouldReturnBaseUriFromOAuthState() {
		HttpServletRequest mockRequest = Mockito.mock(HttpServletRequest.class);
		when(mockRequest.getSession(false)).thenReturn(null);

		String result = RedirectUtils.getRedirectUriFromOAuthState(mockRequest);

		assertEquals(BASE_URI, result);
	}

	@Test
	@DisplayName("속성이 없을 때 /반환")
	void testGetRedirectUri_WithSessionButNoAttribute_ShouldReturnBaseUriFromOAuthState() {
		HttpServletRequest mockRequest = Mockito.mock(HttpServletRequest.class);
		HttpSession mockSession = Mockito.mock(HttpSession.class);

		when(mockRequest.getSession(false)).thenReturn(mockSession);
		when(mockSession.getAttribute(REDIRECT_URI_ATTRIBUTE)).thenReturn(null);

		String result = RedirectUtils.getRedirectUriFromOAuthState(mockRequest);

		assertEquals(BASE_URI, result);
	}

	@Test
	@DisplayName("OAuth2 인증 성공 후 리다이렉트 uri 반환")
	void testGetRedirectUri_FromOAuthState_WithSessionAndAttribute_ShouldReturnAttributeValue() {
		HttpServletRequest mockRequest = Mockito.mock(HttpServletRequest.class);

		when(mockRequest.getParameter("state")).thenReturn("?" + TEST_REDIRECT_URI);

		String result = RedirectUtils.getRedirectUriFromOAuthState(mockRequest);

		assertEquals(TEST_REDIRECT_URI, result);
	}

	@Test
	@DisplayName("파라미터에 리다이렉트 값이 있으면 리다이렉션 uri 반환")
	void testGetRedirectUri_FromParameter_WithRedirectParameter_ShouldReturnAttributeValue() {
		HttpServletRequest mockRequest = Mockito.mock(HttpServletRequest.class);

		when(mockRequest.getParameter(REDIRECT_URI_ATTRIBUTE)).thenReturn(TEST_REDIRECT_URI);

		String result = RedirectUtils.getRedirectUriFromParameter(mockRequest);

		assertEquals(TEST_REDIRECT_URI, result);
	}
}