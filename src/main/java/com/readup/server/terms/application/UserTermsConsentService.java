package com.readup.server.terms.application;

import org.springframework.stereotype.Service;

import com.readup.server.terms.domain.UserTermsConsent;
import com.readup.server.terms.infrastructure.UserTermsConsentJpaRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserTermsConsentService {

	private final UserTermsConsentJpaRepository userTermsConsentJpaRepository;

	@Transactional
	public UserTermsConsent save(UserTermsConsent userTermsConsent) {
		return userTermsConsentJpaRepository.save(userTermsConsent);
	}
}
