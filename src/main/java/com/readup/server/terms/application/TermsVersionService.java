package com.readup.server.terms.application;

import static com.readup.server.common.exception.ErrorCode.*;

import org.springframework.stereotype.Service;

import com.readup.server.common.exception.ServiceException;
import com.readup.server.terms.domain.TermsVersion;
import com.readup.server.terms.infrastructure.TermsVersionJpaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TermsVersionService {

	private final TermsVersionJpaRepository termsVersionJpaRepository;

	public TermsVersion getLatestTermsVersion(Long termsId) {
		return termsVersionJpaRepository.findTopByTermsIdOrderByVersionDesc(termsId)
			.orElseThrow(() -> new ServiceException(TERMS_VERSION_NOT_FOUND));
	}
}
