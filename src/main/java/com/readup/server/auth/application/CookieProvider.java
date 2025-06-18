package com.readup.server.auth.application;

import java.time.Duration;

import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

@Service
public class CookieProvider {

	private static final String ACCESS_TOKEN_NAME = "access_token";
	private static final String REFRESH_TOKEN_NAME = "refresh_token";

	public ResponseCookie generateAccessTokenCookie(String token, int expiresMS) {
		return createCookie(ACCESS_TOKEN_NAME, token, expiresMS);
	}

	public ResponseCookie generateRefreshTokenCookie(String token, int expiresMS) {
		return createCookie(REFRESH_TOKEN_NAME, token, expiresMS);
	}

	public ResponseCookie generateDeletedAccessTokenCookie() {

		return createCookie(ACCESS_TOKEN_NAME, "", 0);
	}

	public ResponseCookie generateDeletedRefreshTokenCookie() {

		return createCookie(REFRESH_TOKEN_NAME, "", 0);
	}

	private ResponseCookie createCookie(String name, String token, int expiresMS) {
		return ResponseCookie.from(name, token)
			.path("/")
			.httpOnly(true) // 보안을 위해 true로 설정 추천
			.secure(true) // HTTPS 환경에서만 전송
			.sameSite("None") // 크로스도메인 쿠키 허용 (필요 시)
			.domain("read-up.kr") // 서브도메인 전체 공유 목적
			.maxAge(Duration.ofMillis(expiresMS))
			.build();
	}
}
