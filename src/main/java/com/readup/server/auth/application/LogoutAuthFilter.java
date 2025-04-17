package com.readup.server.auth.application;

import java.io.IOException;

import org.springframework.lang.NonNull;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.readup.server.auth.infrastructure.RedirectUtils;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class LogoutAuthFilter extends OncePerRequestFilter {
	private final RefreshTokenService refreshTokenService;
	private final CookieProvider cookieProvider;

	private static final RequestMatcher LOGOUT_REQUEST_MATCHER = new AntPathRequestMatcher("/api/public/logout");

	@Override
	protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
		@NonNull FilterChain filterChain) throws ServletException, IOException {

		if (LOGOUT_REQUEST_MATCHER.matches(request)) {
			logout(request, response);
			return;
		}

		filterChain.doFilter(request, response);
	}

	private void logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Cookie[] cookies = request.getCookies();
		String refreshToken = null;

		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if ("refresh_token".equals(cookie.getName())) {
					refreshToken = cookie.getValue();
					break;
				}
			}
		}

		if (refreshToken != null && !refreshToken.isEmpty()) {
			refreshTokenService.removeRefreshToken(refreshToken);
		}

		Cookie deletedAccessCookie = cookieProvider.generateDeletedAccessTokenCookie();
		Cookie deletedRefreshCookie = cookieProvider.generateDeletedRefreshTokenCookie();
		response.addCookie(deletedAccessCookie);
		response.addCookie(deletedRefreshCookie);

		SecurityContextHolder.clearContext();

		response.sendRedirect(RedirectUtils.getRedirectUri(request));
	}
}
