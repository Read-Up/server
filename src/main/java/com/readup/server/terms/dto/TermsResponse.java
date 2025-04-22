package com.readup.server.terms.dto;

import com.readup.server.terms.domain.Terms;
import com.readup.server.terms.domain.TermsVersion;

public record TermsResponse(
	String code,
	String title,
	String content
) {

	public static TermsResponse from(Terms terms, TermsVersion termsVersion) {
		return new TermsResponse(terms.getCode(), terms.getTitle(), termsVersion.getContent());
	}
}
