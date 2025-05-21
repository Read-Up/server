package com.readup.server.user_quiz.presentation;

import static com.readup.server.common.dto.ApiResponse.*;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.readup.server.auth.annotaion.CurrentUser;
import com.readup.server.common.dto.ApiResponse;
import com.readup.server.user.dto.AuthUser;
import com.readup.server.user_quiz.application.UserQuizSetService;
import com.readup.server.user_quiz.application.dto.GetUserQuizSetResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class UserQuizSetController {

	private final UserQuizSetService userQuizSetService;

	@GetMapping("/private/user-quiz-set")
	public ApiResponse<GetUserQuizSetResponse> getUserQuizSet(@RequestParam Long quizSetId,
		@CurrentUser AuthUser user) {
		return successResponse(userQuizSetService.getUserQuizSet(quizSetId, user));
	}
}
