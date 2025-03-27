package com.readup.server.auth.application;

import org.springframework.stereotype.Service;

import com.readup.server.auth.dto.CreateSocialAccountRequest;
import com.readup.server.auth.entity.SocialAccount;
import com.readup.server.auth.infrastructure.SocialAccountRepository;
import com.readup.server.user.entity.User;

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
			.email(createSocialAccountRequest.email())
			.user(user)
			.provider(createSocialAccountRequest.provider())
			.providerUid(createSocialAccountRequest.providerUid())
			.build();
	}
}
