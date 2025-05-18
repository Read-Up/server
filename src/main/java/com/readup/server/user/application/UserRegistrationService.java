package com.readup.server.user.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.readup.server.auth.domain.SocialAccount;
import com.readup.server.auth.domain.SocialAccountService;
import com.readup.server.terms.application.TermsManagementService;
import com.readup.server.terms.domain.UserTermsConsent;
import com.readup.server.user.domain.User;
import com.readup.server.user.domain.UserService;
import com.readup.server.user.domain.UserSocialAccountService;
import com.readup.server.user.dto.CreateUserRequest;
import com.readup.server.user.dto.CreateUserResponse;
import com.readup.server.user.dto.CurrentUser;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserRegistrationService {

	private final UserService userService;
	private final UserSocialAccountService userSocialAccountService;
	private final SocialAccountService socialAccountService;
	private final TermsManagementService termsManagementService;

	public CurrentUser getUserFromSocialAccount(Long socialAccountId) {
		SocialAccount socialAccount = socialAccountService.findById(socialAccountId);
		return CurrentUser.from(socialAccount.getUser());
	}

	@Transactional
	public CreateUserResponse createUser(Long socialAccountId, CreateUserRequest createUserRequest) {

		User user = userSocialAccountService.createSocialAccountUser(
			socialAccountId,
			createUserRequest.nickname()
		);

		List<UserTermsConsent> userTermsConsentList = termsManagementService.createUserTermsConsent(createUserRequest);

		user.updateUserTermsConsentList(userTermsConsentList);

		return CreateUserResponse.from(user);
	}
}
