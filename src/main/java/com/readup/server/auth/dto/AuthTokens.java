package com.readup.server.auth.dto;

public record AuthTokens(
	String accessToken,
	String refreshToken
) {
	public static AuthTokens of(String accessToken, String refreshToken) {
		return new AuthTokens(accessToken, refreshToken);
	}
}
