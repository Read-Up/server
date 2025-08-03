package com.readup.server.user.application;

import java.util.List;

import com.readup.server.auth.infrastructure.SocialAccountJpaRepository;
import com.readup.server.user.domain.repository.UserRepository;
import org.springframework.stereotype.Service;

import com.readup.server.auth.domain.SocialAccount;
import com.readup.server.auth.domain.SocialAccountService;
import com.readup.server.terms.application.TermsManagementService;
import com.readup.server.terms.domain.UserTermsConsent;
import com.readup.server.user.domain.User;
import com.readup.server.user.dto.CreateUserRequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserLifecycleService {
	private final UserRepository userRepository;
	private final TermsManagementService termsManagementService;
	private final SocialAccountService socialAccountService;

	@Transactional
	public void createUser(SocialAccount socialAccount, CreateUserRequest request) {
		SocialAccount attached = socialAccountService.getById(socialAccount.getId());

		List<UserTermsConsent> consentList = termsManagementService.createUserTermsConsent(request);
		User user = User.register(request.nickname(), attached);
		user.addUserTermsConsentList(consentList);
		attached.updateUserFromSocialAccount(user);
		userRepository.save(user);
	}

	@Transactional
	public void deleteUser(User user) {
		userRepository.delete(user);
	}
}
