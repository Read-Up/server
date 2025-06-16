package com.readup.server.auth.infrastructure;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class RedirectUtils {

	private static final String STATE_PARAM = "state";
	private static final String REDIRECT_PARAM = "redirect";
	private static final String DELIMITER = "\\?";
	private static String baseUri;
	private static String signUpUri;
	private static String signInFailUri;

	@Value("${redirect.base-uri}")
	private String baseUriProp;
	@Value("${redirect.sign-up-uri}")
	private String signUpUriProp;
	@Value("${redirect.sign-in-fail-uri}")
	private String signInFailUriProp;

	private RedirectUtils() {
	}

	public static String getSignUpUri() {
		return baseUri + signUpUri;
	}

	public static String getSignInFailUri() {
		return baseUri + signInFailUri;
	}

	public static String getRedirectUriFromOAuthState(HttpServletRequest request) {
		String state = request.getParameter(STATE_PARAM);

		if (state == null || !state.contains("?")) {
			return baseUri;
		}

		String[] parts = state.split(DELIMITER, 2);
		if (parts.length < 2 || parts[1].isBlank()) {
			return baseUri;
		}

		return parts[1];
	}

	public static String getRedirectUriFromParameter(HttpServletRequest request) {
		String redirectUri = request.getParameter(REDIRECT_PARAM);

		if (redirectUri == null || redirectUri.isBlank()) {
			return baseUri;
		}

		return request.getParameter(REDIRECT_PARAM);
	}

	@PostConstruct
	@SuppressWarnings("java:S2696")
	private void init() {
		baseUri = baseUriProp;
		signUpUri = signUpUriProp;
		signInFailUri = signInFailUriProp;
	}
}
