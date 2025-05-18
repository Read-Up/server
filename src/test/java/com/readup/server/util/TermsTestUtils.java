package com.readup.server.util;

import java.util.Arrays;
import java.util.List;

import com.readup.server.terms.domain.Terms;
import com.readup.server.terms.domain.TermsCode;
import com.readup.server.terms.domain.TermsVersion;
import com.readup.server.terms.dto.UserTermsConsentRequest;

public class TermsTestUtils {

	public static Terms createServiceTerms() {
		return Terms.builder()
			.id(1L)
			.code(TermsCode.SERVICE)
			.title("서비스 이용약관")
			.build();
	}

	public static Terms createPrivacyTerms() {
		return Terms.builder()
			.id(2L)
			.code(TermsCode.PRIVACY)
			.title("개인정보 처리방침")
			.build();
	}

	public static Terms createMarketingTerms() {
		return Terms.builder()
			.id(3L)
			.code(TermsCode.MARKETING)
			.title("마케팅 정보 수신동의")
			.build();
	}

	public static List<Terms> createAllTermsList() {
		return Arrays.asList(
			createServiceTerms(),
			createPrivacyTerms(),
			createMarketingTerms()
		);
	}

	public static TermsVersion createServiceTermsVersion() {
		return TermsVersion.builder()
			.id(1L)
			.terms(createServiceTerms())
			.version("1.0")
			.content("서비스 이용약관 내용")
			.build();
	}

	public static TermsVersion createPrivacyTermsVersion() {
		return TermsVersion.builder()
			.id(2L)
			.terms(createPrivacyTerms())
			.version("2.1")
			.content("개인정보 처리방침 내용")
			.build();
	}

	public static TermsVersion createMarketingTermsVersion() {
		return TermsVersion.builder()
			.id(3L)
			.terms(createMarketingTerms())
			.version("1.5")
			.content("마케팅 정보 수신동의 내용")
			.build();

	}

	public static UserTermsConsentRequest createUserServiceTermsConsentRequest() {
		return new UserTermsConsentRequest(1L, TermsCode.SERVICE, true);
	}

	public static UserTermsConsentRequest createUserMarketingTermsConsentRequest() {
		return new UserTermsConsentRequest(3L, TermsCode.PRIVACY, true);
	}

	public static List<UserTermsConsentRequest> createUserTermsConsentList() {
		return Arrays.asList(createUserServiceTermsConsentRequest(), createUserMarketingTermsConsentRequest());
	}
}
