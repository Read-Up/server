package com.readup.server.user.application;

import org.springframework.stereotype.Service;

import com.readup.server.auth.application.SocialAccountService;
import com.readup.server.auth.domain.SocialAccount;
import com.readup.server.user.domain.User;
import com.readup.server.user.dto.CreateUserRequest;
import com.readup.server.user.dto.CreateUserResponse;
import com.readup.server.user.dto.CurrentUser;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserRegistrationService {

	private final UserService userService;
	private final SocialAccountService socialAccountService;

	public CurrentUser getUserFromSocialAccount(Long socialAccountId) {
		SocialAccount socialAccount = socialAccountService.findById(socialAccountId);
		return CurrentUser.from(socialAccount.getUser());
	}

	public CreateUserResponse createUser(Long socialAccountId, CreateUserRequest createUserRequest) {
		User user = userService.save(createUserRequest.nickname());
		socialAccountService.updateUser(socialAccountId, user);
		//약관 동의 row 생성 다음 pr로 넘깁니다. 참고 부탁드려요
		return CreateUserResponse.from(user);
	}
}
