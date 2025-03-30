package com.readup.server.quiz.domain.model.vo;

import java.util.List;

import com.readup.server.quiz.domain.model.Quiz;
import com.readup.server.quiz.domain.model.QuizOption;

import lombok.Builder;

@Builder
public record QuizInfo(
		String question,
		String description,
		Long chapterId,
		List<QuizOptionInfo> options) {
	public Quiz toEntity() {
		Quiz quiz = Quiz.builder()
				.question(question)
				.description(description)
				.chapterId(chapterId)
				.build();

		List<QuizOption> optionEntities = options.stream()
				.map(opt -> opt.toEntity(quiz))
				.toList();

		quiz.getQuizOptions().addAll(optionEntities);
		return quiz;
	}
}
