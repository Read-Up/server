package com.readup.server.quiz.presentation;

import static com.readup.server.common.dto.ApiResponse.*;
import static org.springframework.data.domain.Sort.Direction.*;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.readup.server.auth.dto.CustomOAuth2User;
import com.readup.server.common.dto.ApiResponse;
import com.readup.server.quiz.application.QuizSetService;
import com.readup.server.quiz.application.dto.CreateQuizSetRequest;
import com.readup.server.quiz.application.dto.CreateQuizSetResponse;
import com.readup.server.quiz.application.dto.GetQuizSetPageResponse;
import com.readup.server.quiz.application.dto.GetQuizSetResponse;
import com.readup.server.quiz.application.dto.SliceResponse;

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
		@RequestParam(defaultValue = "0") Long lastQuizId) {
		return successResponse(quizSetService.getQuizSet(quizSetId, lastQuizId));
	}

	@GetMapping("/private/quiz-sets")
	public ApiResponse<SliceResponse<GetQuizSetPageResponse>> getAllQuizSets(
		@RequestParam(required = false) @Min(value = 1, message = "책 ID는 1 이상이어야 합니다.") Long bookId,
		@RequestParam(required = false) @Min(value = 1, message = "챕터 ID는 1 이상이어야 합니다.") Long chapterId,
		@PageableDefault(sort = "likeAverage", direction = DESC) Pageable pageable) {
		return successResponse(quizSetService.getAllQuizSets(bookId, chapterId, pageable));
	}

	@GetMapping("/private/my/quiz-sets")
	public ApiResponse<SliceResponse<GetQuizSetPageResponse>> getMyQuizSets(
		@AuthenticationPrincipal CustomOAuth2User user,
		@RequestParam(required = false) @Min(value = 1, message = "책 ID는 1 이상이어야 합니다.") Long bookId,
		@RequestParam(required = false) @Min(value = 1, message = "챕터 ID는 1 이상이어야 합니다.") Long chapterId,
		@PageableDefault(sort = "createdAt", direction = DESC) Pageable pageable) {
		return successResponse(quizSetService.getMyQuizSets(user.id(), bookId, chapterId, pageable));
	}

	@GetMapping("/private/participating/quiz-sets")
	public ApiResponse<SliceResponse<GetQuizSetPageResponse>> getParticipatingQuizSets(
		@AuthenticationPrincipal CustomOAuth2User user,
		@RequestParam(required = false) @Min(value = 1, message = "책 ID는 1 이상이어야 합니다.") Long bookId,
		@RequestParam(required = false) @Min(value = 1, message = "챕터 ID는 1 이상이어야 합니다.") Long chapterId,
		@PageableDefault(sort = "solvedAt", direction = DESC) Pageable pageable) {
		return successResponse(quizSetService.getParticipatingQuizSets(user.id(), bookId, chapterId, pageable));
	}
}
