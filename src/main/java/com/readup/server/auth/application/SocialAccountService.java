package com.readup.server.auth.application;

import org.springframework.stereotype.Service;

import com.readup.server.auth.domain.CreateSocialAccountRequest;
import com.readup.server.auth.domain.SocialAccount;
import com.readup.server.auth.domain.SocialAccountRepository;
import com.readup.server.user.domain.User;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class SocialAccountService {

	private final SocialAccountRepository socialAccountRepository;

	public SocialAccount save(CreateSocialAccountRequest createSocialAccountRequest) {
		return socialAccountRepository.save(toEntity(createSocialAccountRequest, null));
	}

	private SocialAccount toEntity(CreateSocialAccountRequest createSocialAccountRequest, User user) {
		return SocialAccount.builder()
			.email(createSocialAccountRequest.getEmail())
			.user(user)
			.provider(createSocialAccountRequest.getProvider())
			.providerUid(createSocialAccountRequest.getProviderUid())
			.build();
	}
}
