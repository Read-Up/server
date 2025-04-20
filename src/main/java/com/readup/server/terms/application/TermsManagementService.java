package com.readup.server.terms.application;

import java.util.List;

import org.springframework.stereotype.Service;

import com.readup.server.terms.domain.TermsVersion;
import com.readup.server.terms.dto.TermsResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TermsManagementService {

	private final TermsService termsService;
	private final TermsVersionService termsVersionService;

	public List<TermsResponse> getLatestTermsConsentList() {
		return termsService.findAll().stream().map(terms -> {
			TermsVersion termsVersion = termsVersionService.getLatestTermsVersion(terms.getId());
			return TermsResponse.from(terms, termsVersion);
		}).toList();
	}
}
