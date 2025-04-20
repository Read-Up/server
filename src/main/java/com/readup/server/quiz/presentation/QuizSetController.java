package com.readup.server.quiz.presentation;

import static com.readup.server.common.dto.ApiResponse.*;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.readup.server.common.dto.ApiResponse;
import com.readup.server.quiz.application.QuizSetService;
import com.readup.server.quiz.application.dto.CreateQuizSetRequest;
import com.readup.server.quiz.application.dto.CreateQuizSetResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class QuizSetController {

	private final QuizSetService quizSetService;

	@PostMapping("/private/quiz-sets")
	public ApiResponse<CreateQuizSetResponse> createQuizSet(@RequestBody CreateQuizSetRequest request) {
		return successResponse(quizSetService.createQuizSet(request));
	}
}
