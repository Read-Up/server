package com.readup.server.quiz.presentation;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.readup.server.common.dto.ApiResponse;
import com.readup.server.quiz.application.QuizService;
import com.readup.server.quiz.presentation.dto.CreateQuizRequest;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/quiz")
@RequiredArgsConstructor
public class QuizController {

	private final QuizService quizCommandService;

	@PostMapping
	public ApiResponse<Long> createQuiz(@RequestBody CreateQuizRequest request) {
		Long quizId = quizCommandService.createQuiz(request);
		return ApiResponse.successResponse(quizId);
	}
}
