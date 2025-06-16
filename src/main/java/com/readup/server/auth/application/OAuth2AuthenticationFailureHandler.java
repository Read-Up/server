package com.readup.server.auth.application;

import java.io.IOException;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import com.readup.server.auth.infrastructure.RedirectUtils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

	private static final String OAUTH_ERROR_TYPE = "OAUTH";

	@Override
	public void onAuthenticationFailure(
		HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws
		IOException {

		log.error("Error_Type: {},Description: {}", OAUTH_ERROR_TYPE, exception.getMessage());

		getRedirectStrategy().sendRedirect(request, response, RedirectUtils.getSignInFailUri());
	}
}