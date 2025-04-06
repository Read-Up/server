package com.readup.server.auth.application;

import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

	private RedisTemplate<String, String> redisTemplate;

	private static final String TOKEN_PERFIX = "REFRESH_TOKEN::";

	public void storeRefreshToken(String userId, String refreshToken, long expiresMS) {
		redisTemplate.opsForValue().set(TOKEN_PERFIX + userId, refreshToken, expiresMS, TimeUnit.MILLISECONDS);
	}

	public String getRefreshToken(String userId) {
		return redisTemplate.opsForValue().get(TOKEN_PERFIX + userId);
	}

	public void deleteRefreshToken(String userId) {
		redisTemplate.delete(TOKEN_PERFIX + userId);
	}
}
