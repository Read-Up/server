package com.readup.server.terms.application;

import static com.readup.server.common.exception.ErrorCode.*;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.readup.server.common.exception.ServiceException;
import com.readup.server.terms.domain.TermsVersion;
import com.readup.server.terms.infrastructure.TermsVersionJpaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TermsVersionService {

	private final TermsVersionJpaRepository termsVersionJpaRepository;

	@Transactional(readOnly = true)
	public TermsVersion getLatestTermsVersion(Long termsId, LocalDateTime inquiryDate) {
		return termsVersionJpaRepository.findFirstByTermsIdAndEffectiveDateLessThanEqualOrderByVersionDesc(termsId, inquiryDate)
			.orElseThrow(() -> new ServiceException(TERMS_VERSION_NOT_FOUND));
	}

	@Transactional(readOnly = true)
	public TermsVersion findById(Long id) {
		return termsVersionJpaRepository.findById(id).orElseThrow(() -> new ServiceException(TERMS_VERSION_NOT_FOUND));
	}
}
