package com.readup.server.terms.dto;

import com.readup.server.terms.domain.Terms;
import com.readup.server.terms.domain.TermsCode;
import com.readup.server.terms.domain.TermsVersion;

public record TermsResponse(
	Long termsVersionId,
	TermsCode code,
	String title,
	String content
) {

	public static TermsResponse from(Terms terms, TermsVersion termsVersion) {
		return new TermsResponse(termsVersion.getId(), terms.getCode(), terms.getTitle(), termsVersion.getContent());
	}
}
