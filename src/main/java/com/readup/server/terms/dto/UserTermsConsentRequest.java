package com.readup.server.terms.dto;

public record UserTermsConsentRequest(
	Long termsVersionId,
	String code,
	boolean isConsent
) {
}
