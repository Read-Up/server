package com.readup.server.user.application;

import java.util.List;

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
	private final SocialAccountService socialAccountService;
	private final TermsManagementService termsManagementService;

	public void createUser( SocialAccount socialAccount, CreateUserRequest request) {
		List<UserTermsConsent> consentList = termsManagementService.createUserTermsConsent(request);
		User user = User.register(request.nickname(), socialAccount, request.imageUrl());
		user.addUserTermsConsentList(consentList);
		userRepository.save(user);
	}

	@Transactional
	public void deleteUser(User user) {
		userRepository.delete(user);
	}
}
