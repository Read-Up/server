package com.readup.server.user.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.readup.server.auth.application.SocialAccountService;
import com.readup.server.auth.domain.SocialAccount;
import com.readup.server.terms.application.TermsManagementService;
import com.readup.server.user.domain.User;
import com.readup.server.user.dto.CreateUserRequest;
import com.readup.server.user.dto.CreateUserResponse;
import com.readup.server.user.dto.AuthUser;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserRegistrationService {

	private final UserService userService;
	private final SocialAccountService socialAccountService;
	private final TermsManagementService termsManagementService;

	@Transactional(readOnly = true)
	public AuthUser getUserFromSocialAccount(Long socialAccountId) {
		SocialAccount socialAccount = socialAccountService.findById(socialAccountId);
		return AuthUser.from(socialAccount.getUser());
	}

	public CreateUserResponse createUser(Long socialAccountId, CreateUserRequest createUserRequest) {
		User user = userService.save(createUserRequest.nickname());
		socialAccountService.updateUser(socialAccountId, user);
		termsManagementService.createUserTermsConsent(user, createUserRequest);

		return CreateUserResponse.from(user);
	}
}
