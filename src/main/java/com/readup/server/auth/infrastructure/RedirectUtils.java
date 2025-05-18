package com.readup.server.auth.infrastructure;

import jakarta.servlet.http.HttpServletRequest;

public class RedirectUtils {

	private static final String BASE_URI = "http://localhost:3001";
	private static final String STATE_PARAM = "state";
	private static final String REDIRECT_PARAM = "redirect";
	private static final String DELIMITER = "\\?";
	private static final String SIGN_UP_URI = "/signup";

	private RedirectUtils() {
	}

	public static String getSignUpUri() {
		return BASE_URI + SIGN_UP_URI;
	}

	public static String getRedirectUriFromOAuthState(HttpServletRequest request) {
		String state = request.getParameter(STATE_PARAM);

		if (state == null || !state.contains("?")) {
			return BASE_URI;
		}

		String[] parts = state.split(DELIMITER, 2);
		if (parts.length < 2 || parts[1].isBlank()) {
			return BASE_URI;
		}

		return parts[1];
	}

	public static String getRedirectUriFromParameter(HttpServletRequest request) {
		String redirectUri = request.getParameter(REDIRECT_PARAM);

		if (redirectUri == null || redirectUri.isBlank()) {
			return BASE_URI;
		}
		
		return request.getParameter(REDIRECT_PARAM);
	}
}
