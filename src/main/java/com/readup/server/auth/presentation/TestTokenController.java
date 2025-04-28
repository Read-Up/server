package com.readup.server.auth.presentation;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.readup.server.auth.application.TestTokenService;
import com.readup.server.auth.dto.AuthTokens;
import com.readup.server.common.dto.ApiResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/public/test")
@RequiredArgsConstructor
public class TestTokenController {

	private final TestTokenService testTokenService;

	@GetMapping("/tokens")
	public ApiResponse<AuthTokens> generateTestTokens() {

		return ApiResponse.successResponse(testTokenService.getTestToken(), "Test tokens generated successfully");
	}
}