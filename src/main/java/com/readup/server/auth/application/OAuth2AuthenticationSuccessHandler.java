package com.readup.server.auth.application;

import static com.readup.server.auth.application.TokenProvider.*;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.readup.server.auth.dto.AuthTokens;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

	private final TokenProvider tokenProvider;
	private final CookieProvider cookieProvider;
	private static final String BASE_URI = "/";

	@Override
	public void onAuthenticationSuccess(
		HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws
		IOException, ServletException {

		HttpSession session = request.getSession(false);
		String redirectUri = (session != null && session.getAttribute("redirect_uri") != null) ? (String)session.getAttribute("redirect_uri") : BASE_URI;

		AuthTokens authTokens = tokenProvider.generateTokens(authentication);

		Cookie accessCookie =
			cookieProvider.generateAccessTokenCookie(authTokens.accessToken(), ACCESS_EXPIRY_MS);

		Cookie refreshCookie =
			cookieProvider.generateRefreshTokenCookie(authTokens.refreshToken(), REFRESH_EXPIRY_MS);

		response.addCookie(accessCookie);
		response.addCookie(refreshCookie);

		clearAuthenticationAttributes(request);

		getRedirectStrategy().sendRedirect(request, response, redirectUri);
	}
}
