package com.readup.server.auth.application;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

@ExtendWith(MockitoExtension.class)
class RefreshTokenServiceTest {

	@Mock
	private RedisTemplate<String, String> redisTemplate;

	@Mock
	private ValueOperations<String, String> valueOperations;

	@InjectMocks
	private RefreshTokenService refreshTokenService;

	@BeforeEach
	void setUp() {
		lenient().when(redisTemplate.opsForValue()).thenReturn(valueOperations);
	}

	@Test
	@DisplayName("리프레쉬 토큰 저장하기")
	void testStoreRefreshToken() {
		String userId = "readup";
		String refreshToken = "sampleRefreshToken";
		long expiresMS = 2 * 24 * 60 * 60 * 1000;
		String expectedKey = "REFRESH_TOKEN::" + userId;
		refreshTokenService.storeRefreshToken(userId, refreshToken, expiresMS);

		verify(valueOperations, times(1)).set(expectedKey, refreshToken, expiresMS, TimeUnit.MILLISECONDS);
	}

	@Test
	@DisplayName("리프레쉬 토큰 조회하기")
	void testGetRefreshToken() {
		String userId = "readup";
		String expectedKey = "REFRESH_TOKEN::" + userId;
		String expectedToken = "sampleRefreshToken";

		when(valueOperations.get(expectedKey)).thenReturn(expectedToken);
		String actualToken = refreshTokenService.getRefreshToken(userId);

		assertEquals(expectedToken, actualToken);
	}

	@Test
	@DisplayName("리프레쉬 토큰 삭제하기")
	void testDeleteRefreshToken() {
		String userId = "readup";
		String expectedKey = "REFRESH_TOKEN::" + userId;

		refreshTokenService.deleteRefreshToken(userId);

		verify(redisTemplate, times(1)).delete(expectedKey);
	}
}