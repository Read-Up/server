package com.readup.server.user.domain;

import org.springframework.stereotype.Service;

import com.readup.server.auth.domain.SocialAccount;
import com.readup.server.auth.domain.SocialAccountService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserSocialAccountService {
	private final UserService userService;
	private final SocialAccountService socialAccountService;

	public User createSocialAccountUser(Long socialAccountId, String nickname) {
		userService.existsBySocialAccountId(socialAccountId);
		SocialAccount socialAccount = socialAccountService.getById(socialAccountId);

		User user = User.of(nickname, socialAccount);

		return userService.save(user);
	}
}
