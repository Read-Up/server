package com.readup.server.quiz.domain.model.vo;

import com.readup.server.quiz.domain.model.Quiz;
import com.readup.server.quiz.domain.model.QuizOption;

import lombok.Builder;

@Builder
public record QuizOptionInfo(
		String optionText,
		boolean isCorrect) {
	public QuizOption toEntity(Quiz quiz) {
		return QuizOption.builder()
				.optionText(optionText)
				.isCorrect(isCorrect)
				.quiz(quiz)
				.build();
	}
}
