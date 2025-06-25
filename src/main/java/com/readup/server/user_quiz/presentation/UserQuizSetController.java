package com.readup.server.user_quiz.presentation;

import static com.readup.server.common.dto.ApiResponse.*;

import com.readup.server.common.annotaion.CurrentAuthUser;
import com.readup.server.user.dto.AuthUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.readup.server.common.dto.ApiResponse;
import com.readup.server.user_quiz.application.UserQuizSetService;
import com.readup.server.user_quiz.application.dto.GetUserQuizSetResponse;
import com.readup.server.user_quiz.application.dto.SubmitUserQuizRequest;
import com.readup.server.user_quiz.application.dto.SubmitUserQuizResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class UserQuizSetController {

	private final UserQuizSetService userQuizSetService;

	@GetMapping("/private/quiz-sets/{quizSetId}/my-progress")
	public ApiResponse<GetUserQuizSetResponse> getMyUserQuizSet(@PathVariable Long quizSetId,
																@CurrentAuthUser AuthUser user) {
		return successResponse(userQuizSetService.getUserQuizSet(quizSetId, user));
	}

	@PostMapping("/private/quiz-sets/{quizSetId}/quizzes/{quizId}/answer")
	public ApiResponse<SubmitUserQuizResponse> submitUserQuizAnswer(@PathVariable Long quizSetId,
		@PathVariable Long quizId, @RequestBody SubmitUserQuizRequest request, @CurrentAuthUser AuthUser user) {
		return successResponse(userQuizSetService.submitUserQuizAnswer(quizSetId, quizId, request, user));
	}
}
