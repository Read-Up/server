package com.readup.server.user_quiz.presentation;

import static com.readup.server.common.dto.ApiResponse.*;

import java.time.LocalDateTime;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.readup.server.auth.dto.CustomOAuth2User;
import com.readup.server.common.dto.ApiResponse;
import com.readup.server.user_quiz.application.UserQuizSetService;
import com.readup.server.user_quiz.application.dto.CompleteUserQuizSetResponse;
import com.readup.server.user_quiz.application.dto.GetUserQuizSetResponse;
import com.readup.server.user_quiz.application.dto.GetUserQuizSetResultResponse;
import com.readup.server.user_quiz.application.dto.StartUserQuizSetResponse;
import com.readup.server.user_quiz.application.dto.SubmitUserQuizRequest;
import com.readup.server.user_quiz.application.dto.SubmitUserQuizResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class UserQuizSetController {

	private final UserQuizSetService userQuizSetService;

	@GetMapping("/private/quiz-sets/{quizSetId}/my-progress")
	public ApiResponse<GetUserQuizSetResponse> getMyUserQuizSet(@PathVariable Long quizSetId,
		@AuthenticationPrincipal CustomOAuth2User user) {
		return successResponse(userQuizSetService.getUserQuizSet(quizSetId, user.id()));
	}

	@GetMapping("/private/user-quiz-sets/{userQuizSetId}/result")
	public ApiResponse<GetUserQuizSetResultResponse> getMyUserQuizSetResult(@PathVariable Long userQuizSetId,
		@AuthenticationPrincipal CustomOAuth2User user) {
		return successResponse(userQuizSetService.getUserQuizSetResult(userQuizSetId, user.id()));
	}

	@PostMapping("/private/user-quiz-sets/{userQuizSetId}/start")
	public ApiResponse<StartUserQuizSetResponse> startUserQuizSet(@PathVariable Long userQuizSetId,
		@AuthenticationPrincipal CustomOAuth2User user) {
		return successResponse(userQuizSetService.startUserQuizSet(userQuizSetId, user.id(), LocalDateTime.now()));
	}

	@PostMapping("/private/user-quiz-sets/{userQuizSetId}/complete")
	public ApiResponse<CompleteUserQuizSetResponse> completeUserQuizSet(@PathVariable Long userQuizSetId,
		@AuthenticationPrincipal CustomOAuth2User user) {
		return successResponse(userQuizSetService.completeUserQuizSet(userQuizSetId, user.id(), LocalDateTime.now()));
	}

	@PostMapping("/private/quiz-sets/{quizSetId}/quizzes/{quizId}/answer")
	public ApiResponse<SubmitUserQuizResponse> submitUserQuizAnswer(@PathVariable Long quizSetId,
		@PathVariable Long quizId, @AuthenticationPrincipal CustomOAuth2User user,
		@RequestBody SubmitUserQuizRequest request) {
		return successResponse(userQuizSetService.submitUserQuizAnswer(quizSetId, quizId, request, user.id()));
	}
}
