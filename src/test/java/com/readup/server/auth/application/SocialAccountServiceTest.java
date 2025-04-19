package com.readup.server.auth.application;

import static com.readup.server.common.exception.ErrorCode.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.readup.server.auth.domain.SocialAccount;
import com.readup.server.auth.dto.CreateSocialAccountRequest;
import com.readup.server.auth.infrastructure.SocialAccountRepository;
import com.readup.server.common.exception.ServiceException;

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

	@Test
	@DisplayName("id로 계정 찾기 성공")
	void findByIdTest() {
		SocialAccount socialAccount = SocialAccount.builder()
			.id(1L)
			.email("readup@readup.com")
			.provider("google")
			.providerUid("readup")
			.build();

		when(socialAccountRepository.findById(1L)).thenReturn(Optional.ofNullable(socialAccount));

		SocialAccount result = socialAccountService.findById(1L);

		assertNotNull(result);
		assertEquals(1L, result.getId());
		verify(socialAccountRepository, times(1)).findById(1L);
	}

	@Test
	@DisplayName("id로 계정 찾기 실패")
	void findByIdExceptionTest() {
		when(socialAccountRepository.findById(1L)).thenReturn(Optional.empty());

		ServiceException exception = assertThrows(ServiceException.class, () -> {
			socialAccountService.findById(1L);
		});

		assertEquals(SOCIAL_ACCOUNT_NOT_FOUND, exception.getErrorCode());
		verify(socialAccountRepository, times(1)).findById(1L);
	}
}