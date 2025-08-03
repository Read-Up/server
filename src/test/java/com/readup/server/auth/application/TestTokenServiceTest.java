package com.readup.server.auth.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.readup.server.user.UserTestBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;

import com.readup.server.auth.domain.SocialAccount;
import com.readup.server.auth.domain.SocialAccountService;
import com.readup.server.auth.dto.AuthTokens;
import com.readup.server.user.domain.User;

@ExtendWith(MockitoExtension.class)
class TestTokenServiceTest {

	@Mock
	private TokenProvider tokenProvider;

	@Mock
	private SocialAccountService socialAccountService;

	@InjectMocks
	private TestTokenService testTokenService;

	private SocialAccount testAccount;
	private AuthTokens expectedTokens;

	@BeforeEach
	void setUp() {
		User user = UserTestBuilder.builder().id(1L).nickname("지적인 도마뱀").build();

		testAccount = SocialAccount.builder()
			.id(1L)
			.user(user)
			.email("test@example.com")
			.provider("test-provider")
			.providerUid("test-provider-uid")
			.build();

		when(socialAccountService.getById(1L)).thenReturn(testAccount);

		expectedTokens = new AuthTokens("access-token-value", "refresh-token-value");

		when(tokenProvider.generateTokens(any(OAuth2AuthenticationToken.class)))
			.thenReturn(expectedTokens);
	}

	@Test
	void getTestToken_ShouldReturnTokens() {
		AuthTokens result = testTokenService.getTestToken();

		assertThat(result).isNotNull().isEqualTo(expectedTokens);

		verify(socialAccountService).getById(1L);

		verify(tokenProvider).generateTokens(any(OAuth2AuthenticationToken.class));
	}
}
