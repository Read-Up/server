package com.readup.server.terms.application;

import static com.readup.server.common.exception.ErrorCode.*;
import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
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
		// 고정된 날짜 사용
		LocalDateTime fixedDateTime = LocalDateTime.of(2025, 4, 22, 10, 0);
		Long termsId = 1L;

		TermsVersion serviceTermsVersion = TermsTestUtils.createServiceTermsVersion();

		when(termsVersionJpaRepository.findFirstByTermsIdAndEffectiveDateLessThanEqualOrderByVersionDesc(
			eq(termsId), any(LocalDateTime.class)))
			.thenReturn(Optional.ofNullable(serviceTermsVersion));

		TermsVersion result = termsVersionService.getLatestTermsVersion(termsId, fixedDateTime);

		assertThat(result).isNotNull();
		assertThat(result.getId()).isEqualTo(1L);
		assertThat(result.getTerms()).isEqualTo(serviceTermsVersion.getTerms());
		assertThat(result.getVersion()).isEqualTo(serviceTermsVersion.getVersion());
		assertThat(result.getContent()).isEqualTo(serviceTermsVersion.getContent());

		verify(termsVersionJpaRepository, times(1)).findFirstByTermsIdAndEffectiveDateLessThanEqualOrderByVersionDesc(
			eq(termsId), any(LocalDateTime.class));
	}

	@Test
	@DisplayName("존재하지 않는 약관 ID로 조회 시 예외 발생")
	void getLatestTermsVersion_NotFound_ThrowsException() {
		// 고정된 날짜 사용
		LocalDateTime fixedDateTime = LocalDateTime.of(2025, 4, 22, 10, 0);
		Long nonExistentTermsId = 999L;

		when(termsVersionJpaRepository.findFirstByTermsIdAndEffectiveDateLessThanEqualOrderByVersionDesc(
			eq(nonExistentTermsId), any(LocalDateTime.class)))
			.thenReturn(Optional.empty());

		assertThatThrownBy(() -> termsVersionService.getLatestTermsVersion(nonExistentTermsId, fixedDateTime))
			.isInstanceOf(ServiceException.class)
			.hasFieldOrPropertyWithValue("errorCode", TERMS_VERSION_NOT_FOUND);

		verify(termsVersionJpaRepository, times(1)).findFirstByTermsIdAndEffectiveDateLessThanEqualOrderByVersionDesc(
			eq(nonExistentTermsId), any(LocalDateTime.class));
	}
}