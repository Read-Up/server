package com.readup.server.user.dto;

import java.util.List;

import com.readup.server.terms.dto.UserTermsConsentRequest;

public record CreateUserRequest(
	String nickname,
	List<UserTermsConsentRequest> termsConsentRequestList
) {
}
