package com.readup.server.auth.application;

import java.util.Collections;
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Service;

import com.readup.server.auth.domain.SocialAccount;
import com.readup.server.auth.domain.SocialAccountService;
import com.readup.server.auth.dto.AuthTokens;
import com.readup.server.auth.dto.CustomOAuth2User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TestTokenService {

	private final TokenProvider tokenProvider;
	private final SocialAccountService socialAccountService;

	public AuthTokens getTestToken() {
		SocialAccount testAccount = socialAccountService.findById(1L);

		Map<String, Object> attributes = Collections.singletonMap("sub", "1");
		CustomOAuth2User testUser = CustomOAuth2User.from(testAccount, attributes);

		Authentication authentication = new OAuth2AuthenticationToken(
			testUser,
			testUser.getAuthorities(),
			"test-provider"
		);

		return tokenProvider.generateTokens(authentication);
	}
}
