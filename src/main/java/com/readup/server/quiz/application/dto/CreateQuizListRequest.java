package com.readup.server.quiz.application.dto;

import java.util.List;

import com.readup.server.book.domain.Chapter;
import com.readup.server.quiz.domain.model.Quiz;

public record CreateQuizListRequest(
	Long bookId, Long chapterId,
	List<CreateQuizRequest> quizRequestList) {

	public record CreateQuizRequest(
		String question, String explanation,
		List<CreateQuizOptionRequest> quizOptionRequestList) {

		public Quiz toEntity(Chapter chapter) {
			Quiz quiz = Quiz.create(chapter, question, explanation);
			quizOptionRequestList.forEach(qor -> qor.addTo(quiz));
			return quiz;
		}

		public record CreateQuizOptionRequest(
			Integer number, String content, Boolean isCorrect) {

			private void addTo(Quiz quiz) {
				quiz.addQuizOption(number, content, isCorrect);

				if (Boolean.TRUE.equals(isCorrect)) {
					quiz.addAnswerOptionNumber(number);
				}
			}
		}
	}
}
