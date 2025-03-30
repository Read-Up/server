package com.readup.server.quiz.presentation.dto;

import java.util.List;

import com.readup.server.quiz.domain.model.vo.QuizInfo;
import com.readup.server.quiz.domain.model.vo.QuizOptionInfo;

public record CreateQuizRequest(
		String question,
		String description,
		Long chapterId,
		List<QuizOptionRequest> options) {
	public QuizInfo toValueObject() {
		List<QuizOptionInfo> optionInfos = options.stream()
				.map(opt -> new QuizOptionInfo(opt.optionText(), opt.isCorrect()))
				.toList();

		return new QuizInfo(question, description, chapterId, optionInfos);
	}

	public record QuizOptionRequest(
			String optionText,
			boolean isCorrect) {
	}
}
