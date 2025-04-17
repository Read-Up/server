package com.readup.server.auth.application;

import static com.readup.server.common.exception.ErrorCode.*;

import org.springframework.stereotype.Service;

import com.readup.server.auth.domain.SocialAccount;
import com.readup.server.auth.dto.CreateSocialAccountRequest;
import com.readup.server.auth.infrastructure.SocialAccountRepository;
import com.readup.server.common.exception.ServiceException;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class SocialAccountService {

	private final SocialAccountRepository socialAccountRepository;

	public SocialAccount save(CreateSocialAccountRequest createSocialAccountRequest) {
		return socialAccountRepository.save(toEntity(createSocialAccountRequest));
	}

	public SocialAccount findById(Long id) {
		return socialAccountRepository.findById(id).orElseThrow(() -> new ServiceException(SOCIAL_ACCOUNT_NOT_FOUND));
	}

	private SocialAccount toEntity(CreateSocialAccountRequest createSocialAccountRequest) {
		return SocialAccount.builder()
			.email(createSocialAccountRequest.email())
			.provider(createSocialAccountRequest.provider())
			.providerUid(createSocialAccountRequest.providerUid())
			.build();
	}
}
