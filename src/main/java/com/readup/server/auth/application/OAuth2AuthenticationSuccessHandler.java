package com.readup.server.auth.application;

import static com.readup.server.auth.application.TokenProvider.*;

import java.io.IOException;

import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.readup.server.auth.dto.AuthTokens;
import com.readup.server.auth.infrastructure.RedirectUtils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

	private final TokenProvider tokenProvider;
	private final CookieProvider cookieProvider;

	@Override
	public void onAuthenticationSuccess(
		@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Authentication authentication) throws
		IOException {

		SecurityContextHolder.getContext().setAuthentication(authentication);

		AuthTokens authTokens = tokenProvider.generateTokens(authentication);

		Cookie accessCookie =
			cookieProvider.generateAccessTokenCookie(authTokens.accessToken(), ACCESS_EXPIRY_MS);

		Cookie refreshCookie =
			cookieProvider.generateRefreshTokenCookie(authTokens.refreshToken(), REFRESH_EXPIRY_MS);

		response.addCookie(accessCookie);
		response.addCookie(refreshCookie);

		clearAuthenticationAttributes(request);

		getRedirectStrategy().sendRedirect(request, response, RedirectUtils.getRedirectUri(request));
	}
}
