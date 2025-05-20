package com.readup.server.terms.dto;

import com.readup.server.terms.domain.TermsCode;

public record UserTermsConsentRequest(
	Long termsVersionId,
	TermsCode code,
	boolean isConsent
) {
}
