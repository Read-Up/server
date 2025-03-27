package com.readup.server.auth.application;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.readup.server.auth.dto.CreateSocialAccountRequest;
import com.readup.server.auth.domain.SocialAccount;
import com.readup.server.auth.infrastructure.SocialAccountRepository;

@ExtendWith(MockitoExtension.class)
class SocialAccountServiceTest {

	@Mock
	private SocialAccountRepository socialAccountRepository;

	@InjectMocks
	private SocialAccountService socialAccountService;

	@Test
	@DisplayName(value = "social account 저장하기")
	void saveTest_success() {

		CreateSocialAccountRequest createSocialAccountRequest = CreateSocialAccountRequest.of(
			"readup@readup.com", "google", "readup"
		);

		SocialAccount socialAccount = SocialAccount.builder()
			.email("readup@readup.com")
			.provider("google")
			.providerUid("readup")
			.build();

		when(socialAccountRepository.save(any(SocialAccount.class))).thenReturn(socialAccount);

		SocialAccount result = socialAccountService.save(createSocialAccountRequest);
		assertNotNull(result);
		assertEquals(socialAccount.getProvider(), result.getProvider());
		assertEquals(socialAccount.getProviderUid(), result.getProviderUid());
		assertEquals(socialAccount.getEmail(), result.getEmail());
		verify(socialAccountRepository, times(1)).save(any(SocialAccount.class));
	}
}