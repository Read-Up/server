package com.readup.server.user.presentation;

import static com.readup.server.common.dto.ApiResponse.*;
import static com.readup.server.user.generator.RandomNicknameGenerator.*;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.readup.server.auth.annotaion.CurrentSocialAccountId;
import com.readup.server.common.dto.ApiResponse;
import com.readup.server.user.application.UserRegistrationService;
import com.readup.server.user.dto.CreateUserRequest;
import com.readup.server.user.dto.CreateUserResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class UserController {

	private final UserRegistrationService userRegistrationService;

	@GetMapping("/public/users/random-nickname")
	public ApiResponse<String> getRandomNickname() {
		return successResponse(generate());
	}

	@PostMapping("/private/users/signup")
	public ApiResponse<CreateUserResponse> createUser(@CurrentSocialAccountId Long socialAccountId, @RequestBody CreateUserRequest createUserRequest) {
		return successResponse(userRegistrationService.createUser(socialAccountId, createUserRequest));
	}
}
