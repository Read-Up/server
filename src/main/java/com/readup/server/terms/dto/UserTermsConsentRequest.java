package com.readup.server.terms.dto;

public record UserTermsConsentRequest(
	String code,
	boolean isConsent
) {
}
