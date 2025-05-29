package com.readup.server.quiz.application.dto;

import java.util.List;

import com.readup.server.quiz.domain.model.QuizSet;

public record CreateQuizSetRequest(
	Long bookId, Long chapterId,
	List<CreateQuizRequest> quizRequestList) {

	public QuizSet toEntity() {
		return QuizSet.create(bookId, chapterId, quizRequestList);
	}

	public record CreateQuizRequest(
		String question, String explanation,
		List<CreateQuizOptionRequest> quizOptionRequestList) {

		public record CreateQuizOptionRequest(String content, Boolean isCorrect) {
		}
	}
}
