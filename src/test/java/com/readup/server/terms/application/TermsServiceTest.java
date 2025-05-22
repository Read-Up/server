package com.readup.server.terms.application;

import static com.readup.server.common.exception.ErrorCode.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.readup.server.common.exception.ServiceException;
import com.readup.server.terms.domain.Terms;
import com.readup.server.terms.domain.TermsCode;
import com.readup.server.terms.infrastructure.TermsJpaRepository;
import com.readup.server.util.TermsTestUtils;

@ExtendWith(MockitoExtension.class)
class TermsServiceTest {

	@Mock
	TermsJpaRepository termsJpaRepository;

	@InjectMocks
	TermsService termsService;

	@Test
	@DisplayName("약관 모두 조회하기")
	void testFindAll() {
		List<Terms> termsList = TermsTestUtils.createAllTermsList();

		when(termsJpaRepository.findAll()).thenReturn(termsList);

		List<Terms> result = termsService.findAll();

		assertThat(result)
			.hasSize(3)
			.containsExactlyElementsOf(termsList);

		Terms serviceTerm = termsList.get(0);
		Terms privacyTerm = termsList.get(1);
		Terms marketingTerm = termsList.get(2);

		assertThat(result.get(0).getId()).isEqualTo(serviceTerm.getId());
		assertThat(result.get(0).getTitle()).isEqualTo(serviceTerm.getTitle());

		assertThat(result.get(1).getId()).isEqualTo(privacyTerm.getId());
		assertThat(result.get(1).getTitle()).isEqualTo(privacyTerm.getTitle());

		assertThat(result.get(2).getId()).isEqualTo(marketingTerm.getId());
		assertThat(result.get(2).getTitle()).isEqualTo(marketingTerm.getTitle());

		verify(termsJpaRepository, times(1)).findAll();
	}

	@Test
	@DisplayName("약관이 없는 경우 예외 발생")
	void findAll_EmptyList_ThrowsException() {
		when(termsJpaRepository.findAll()).thenReturn(Collections.emptyList());

		assertThatThrownBy(() -> termsService.findAll())
			.isInstanceOf(ServiceException.class)
			.hasFieldOrPropertyWithValue("errorCode", TERMS_NOT_FOUND);

		verify(termsJpaRepository, times(1)).findAll();
	}

	@Test
	@DisplayName("코드로 약관 조회")
	void testFindByCode() {
		Terms expectedTerm = TermsTestUtils.createServiceTerms();
		when(termsJpaRepository.findByCode(TermsCode.SERVICE)).thenReturn(Optional.ofNullable(expectedTerm));

		Terms serviceTerms = termsService.findByCode(TermsCode.SERVICE);

		assertThat(serviceTerms).isNotNull();
		assertThat(serviceTerms.getId()).isEqualTo(expectedTerm.getId());
		assertThat(serviceTerms.getCode()).isEqualTo(expectedTerm.getCode());
		assertThat(serviceTerms.getTitle()).isEqualTo(expectedTerm.getTitle());

		verify(termsJpaRepository, times(1)).findByCode(TermsCode.SERVICE);
	}

	@Test
	@DisplayName("코드없는 경우 예외발생")
	void findByCode_ThrowsException() {
		when(termsJpaRepository.findByCode(TermsCode.SERVICE)).thenReturn(Optional.empty());

		assertThatThrownBy(() -> termsService.findByCode(TermsCode.SERVICE))
			.isInstanceOf(ServiceException.class)
			.hasFieldOrPropertyWithValue("errorCode", TERMS_NOT_FOUND);

		verify(termsJpaRepository, times(1)).findByCode(TermsCode.SERVICE);
	}
}