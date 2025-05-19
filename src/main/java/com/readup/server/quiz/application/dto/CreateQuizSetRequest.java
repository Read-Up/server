package com.readup.server.quiz.application.dto;

import java.util.List;

import com.readup.server.quiz.domain.model.Quiz;
import com.readup.server.quiz.domain.model.QuizSet;

public record CreateQuizSetRequest(
	Long bookId, Long chapterId,
	List<CreateQuizRequest> quizRequestList) {

	public QuizSet toEntity() {
		QuizSet quizSet = QuizSet.create(bookId, chapterId);

		quizRequestList.forEach(qr -> {
			Quiz quiz = quizSet.addQuiz(qr.question, qr.explanation);
			qr.quizOptionRequestList.forEach(qor -> quiz.addQuizOption(qor.content, qor.isCorrect));
		});

		return quizSet;
	}

	public record CreateQuizRequest(
		String question, String explanation,
		List<CreateQuizOptionRequest> quizOptionRequestList) {

		public record CreateQuizOptionRequest(String content, Boolean isCorrect) {
		}
	}
}
