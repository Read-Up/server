package com.readup.server.quiz.presentation;

import static com.readup.server.common.dto.ApiResponse.*;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.readup.server.common.dto.ApiResponse;
import com.readup.server.quiz.application.QuizSetService;
import com.readup.server.quiz.application.dto.CreateQuizSetRequest;
import com.readup.server.quiz.application.dto.CreateQuizSetResponse;
import com.readup.server.quiz.application.dto.GetQuizSetResponse;

import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class QuizSetController {

	private final QuizSetService quizSetService;

	@PostMapping("/private/quiz-sets")
	public ApiResponse<CreateQuizSetResponse> createQuizSet(@RequestBody CreateQuizSetRequest request) {
		return successResponse(quizSetService.createQuizSet(request));
	}

	@GetMapping("/private/quiz-sets/{quizSetId}")
	public ApiResponse<GetQuizSetResponse> getQuizSet(@PathVariable Long quizSetId,
		@RequestParam(defaultValue = "1") @Min(value = 1, message = "퀴즈 시작 번호는 1부터 시작합니다.") int startQuizSequence) {
		return successResponse(quizSetService.getQuizSet(quizSetId, startQuizSequence));
	}
}
