package com.readup.server.terms.application;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.readup.server.terms.domain.Terms;
import com.readup.server.terms.domain.TermsVersion;
import com.readup.server.terms.domain.UserTermsConsent;
import com.readup.server.terms.dto.TermsResponse;
import com.readup.server.terms.dto.UserTermsConsentRequest;
import com.readup.server.user.dto.CreateUserRequest;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TermsManagementService {

	private final TermsService termsService;
	private final TermsVersionService termsVersionService;
	private final UserTermsConsentService userTermsConsentService;

	@Transactional(readOnly = true)
	public List<TermsResponse> getLatestTermsConsentList() {
		return termsService.findAll().stream().map(terms -> {
			TermsVersion termsVersion = termsVersionService.getLatestTermsVersion(terms.getId(), LocalDateTime.now());
			return TermsResponse.from(terms, termsVersion);
		}).toList();
	}

	@Transactional
	public List<UserTermsConsent> createUserTermsConsent(CreateUserRequest createUserRequest) {
		return createUserRequest.termsConsentRequestList().stream()
			.map(this::toEntity).toList();
	}

	private UserTermsConsent toEntity(UserTermsConsentRequest userTermsConsentRequest) {
		Terms terms = termsService.findByCode(userTermsConsentRequest.code());
		TermsVersion termsVersion = termsVersionService.findById(userTermsConsentRequest.termsVersionId());
		return UserTermsConsent.builder()
			.isConsent(userTermsConsentRequest.isConsent())
			.termsVersion(termsVersion)
			.terms(terms)
			.build();
	}
}
