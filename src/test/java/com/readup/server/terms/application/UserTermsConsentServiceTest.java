package com.readup.server.terms.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.readup.server.user.UserTestBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.readup.server.terms.domain.Terms;
import com.readup.server.terms.domain.TermsVersion;
import com.readup.server.terms.domain.UserTermsConsent;
import com.readup.server.terms.infrastructure.UserTermsConsentJpaRepository;
import com.readup.server.user.domain.User;
import com.readup.server.util.TermsTestUtils;

@ExtendWith(MockitoExtension.class)
class UserTermsConsentServiceTest {

	@Mock
	private UserTermsConsentJpaRepository userTermsConsentJpaRepository;

	@InjectMocks
	private UserTermsConsentService userTermsConsentService;

	@BeforeEach
	void setUp() {
		userTermsConsentService = new UserTermsConsentService(userTermsConsentJpaRepository);
	}

	@Test
	@DisplayName("사용자 약관 동의 여부 저장")
	void testSave_WhenValidUserTermsConsent() {
		User user = UserTestBuilder.builder()
			.id(1L)
			.nickname("지적인 도마뱀")
			.build();
		Terms terms = TermsTestUtils.createMarketingTerms();
		TermsVersion marketingTermsVersion = TermsTestUtils.createMarketingTermsVersion();
		UserTermsConsent userTermsConsent = new UserTermsConsent(1L, user, terms, marketingTermsVersion, true);

		when(userTermsConsentJpaRepository.save(userTermsConsent))
			.thenReturn(userTermsConsent);

		UserTermsConsent savedUserTermsConsent = userTermsConsentService.save(userTermsConsent);

		assertThat(savedUserTermsConsent).isNotNull();
		assertThat(savedUserTermsConsent.getId()).isEqualTo(1L);
		assertThat(savedUserTermsConsent.getUser()).isEqualTo(user);
		assertThat(savedUserTermsConsent.getTerms()).isEqualTo(terms);
		assertThat(savedUserTermsConsent.getTermsVersion()).isEqualTo(marketingTermsVersion);
		assertThat(savedUserTermsConsent.getIsConsent()).isEqualTo(true);

		verify(userTermsConsentJpaRepository, times(1)).save(userTermsConsent);
	}
}