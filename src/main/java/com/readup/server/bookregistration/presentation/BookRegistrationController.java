package com.readup.server.bookregistration.presentation;

import static com.readup.server.common.dto.ApiResponse.*;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.readup.server.bookregistration.application.BookRegistrationService;
import com.readup.server.bookregistration.presentation.dto.CreateBookRegistrationRequest;
import com.readup.server.bookregistration.presentation.dto.CreateBookRegistrationResponse;
import com.readup.server.common.dto.ApiResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class BookRegistrationController {

	private final BookRegistrationService bookRegistrationService;

	@PostMapping("/private/book-registrations")
	public ApiResponse<CreateBookRegistrationResponse> createBookRegistration(@AuthenticationPrincipal Long userId,
		@RequestBody CreateBookRegistrationRequest request) {
		return successResponse(bookRegistrationService.createBookRegistration(userId, request));
	}
}
