package com.readup.server.auth.domain;

import static com.readup.server.common.exception.ErrorCode.*;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.readup.server.auth.dto.CreateSocialAccountRequest;
import com.readup.server.auth.dto.CustomOAuth2User;
import com.readup.server.auth.infrastructure.SocialAccountJpaRepository;
import com.readup.server.common.exception.DomainException;
import com.readup.server.user.domain.User;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class SocialAccountService {

	private final SocialAccountJpaRepository socialAccountJpaRepository;

	public SocialAccount saveOrGet(CreateSocialAccountRequest createSocialAccountRequest) {
		return socialAccountJpaRepository
			.findByProviderAndProviderUid(
				createSocialAccountRequest.provider(),
				createSocialAccountRequest.providerUid()
			)
			.orElseGet(() -> save(createSocialAccountRequest));
	}

	public SocialAccount save(CreateSocialAccountRequest createSocialAccountRequest) {
		return socialAccountJpaRepository.save(toEntity(createSocialAccountRequest));
	}

	public SocialAccount findById(Long id) {
		return socialAccountJpaRepository.findById(id)
			.orElseThrow(() -> new DomainException(SOCIAL_ACCOUNT_NOT_FOUND));
	}

	public void updateUser(Long socialAccountId, User user) {
		SocialAccount savedSocialAccount = findById(socialAccountId);
		savedSocialAccount.updateUserFromSocialAccount(user);
	}

	public boolean isNewUser(Authentication authentication) {
		CustomOAuth2User oAuth2User = (CustomOAuth2User)authentication.getPrincipal();

		return findById(Long.parseLong(oAuth2User.getName())).getUser() == null;
	}

	private SocialAccount toEntity(CreateSocialAccountRequest createSocialAccountRequest) {
		return SocialAccount.builder()
			.email(createSocialAccountRequest.email())
			.provider(createSocialAccountRequest.provider())
			.providerUid(createSocialAccountRequest.providerUid())
			.build();
	}

	public SocialAccount getById(Long socialAccountId) {
		return socialAccountJpaRepository.findById(socialAccountId)
			.orElseThrow(() -> new DomainException(SOCIAL_ACCOUNT_NOT_FOUND));
	}
}
