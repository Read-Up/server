package com.readup.server.terms.presentation;

import static com.readup.server.common.dto.ApiResponse.*;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.readup.server.common.dto.ApiResponse;
import com.readup.server.terms.application.TermsManagementService;
import com.readup.server.terms.dto.TermsResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class TermsController {

	private final TermsManagementService termsManagementService;

	@GetMapping("/public/terms")
	public ApiResponse<List<TermsResponse>> getTermsList() {
		return successResponse(termsManagementService.getLatestTermsConsentList());
	}
}
