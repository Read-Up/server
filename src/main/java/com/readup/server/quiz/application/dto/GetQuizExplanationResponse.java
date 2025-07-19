package com.readup.server.quiz.application.dto;

import com.readup.server.quiz.domain.model.Quiz;

public record GetQuizExplanationResponse(Long quizId, String explanation) {

	public static GetQuizExplanationResponse from(Quiz quiz) {
		return new GetQuizExplanationResponse(quiz.getId(), quiz.getExplanation());
	}
}
