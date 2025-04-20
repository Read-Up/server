package com.readup.server.terms.application;

import static com.readup.server.common.exception.ErrorCode.*;
import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.readup.server.common.exception.ServiceException;
import com.readup.server.terms.domain.TermsVersion;
import com.readup.server.terms.infrastructure.TermsVersionJpaRepository;
import com.readup.server.util.TermsTestUtils;

@ExtendWith(MockitoExtension.class)
class TermsVersionServiceTest {

	@Mock
	TermsVersionJpaRepository termsVersionJpaRepository;

	@InjectMocks
	TermsVersionService termsVersionService;

	@Test
	@DisplayName("약관 ID로 최신 버전 조회 성공")
	void getLatestTermsVersion_Success() {
		Long termsId = 1L;

		TermsVersion serviceTermsVersion = TermsTestUtils.createServiceTermsVersion();

		when(termsVersionJpaRepository.findTopByTermsIdOrderByVersionDesc(termsId))
			.thenReturn(Optional.ofNullable(serviceTermsVersion));

		TermsVersion result = termsVersionService.getLatestTermsVersion(termsId);

		assertThat(result).isNotNull();
		assertThat(result.getId()).isEqualTo(1L);
		assertThat(result.getTerms()).isEqualTo(serviceTermsVersion.getTerms());
		assertThat(result.getVersion()).isEqualTo(serviceTermsVersion.getVersion());
		assertThat(result.getContent()).isEqualTo(serviceTermsVersion.getContent());

		verify(termsVersionJpaRepository, times(1)).findTopByTermsIdOrderByVersionDesc(termsId);
	}

	@Test
	@DisplayName("존재하지 않는 약관 ID로 조회 시 예외 발생")
	void getLatestTermsVersion_NotFound_ThrowsException() {
		Long nonExistentTermsId = 999L;
		when(termsVersionJpaRepository.findTopByTermsIdOrderByVersionDesc(nonExistentTermsId))
			.thenReturn(Optional.empty());

		assertThatThrownBy(() -> termsVersionService.getLatestTermsVersion(nonExistentTermsId))
			.isInstanceOf(ServiceException.class)
			.hasFieldOrPropertyWithValue("errorCode", TERMS_VERSION_NOT_FOUND);

		verify(termsVersionJpaRepository, times(1)).findTopByTermsIdOrderByVersionDesc(nonExistentTermsId);
	}
}