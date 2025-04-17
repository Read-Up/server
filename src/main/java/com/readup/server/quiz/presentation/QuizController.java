package com.readup.server.quiz.presentation;

import static com.readup.server.common.dto.ApiResponse.*;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.readup.server.common.dto.ApiResponse;
import com.readup.server.quiz.application.QuizService;
import com.readup.server.quiz.application.dto.CreateQuizListRequest;
import com.readup.server.quiz.application.dto.CreateQuizListResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class QuizController {

	private final QuizService quizService;

	@PostMapping("/private/quizzes")
	public ApiResponse<CreateQuizListResponse> createQuiz(@RequestBody CreateQuizListRequest request) {
		return successResponse(quizService.createQuiz(request));
	}
}
