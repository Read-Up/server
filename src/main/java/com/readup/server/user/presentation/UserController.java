package com.readup.server.user.presentation;

import static com.readup.server.common.dto.ApiResponse.*;
import static com.readup.server.user.generator.RandomNicknameGenerator.*;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.readup.server.common.dto.ApiResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class UserController {

	@GetMapping("/public/users/random-nickname")
	public ApiResponse<String> getRandomNickname() {
		return successResponse(generate());
	}
}
