package com.readup.server.quiz.application.dto;

import java.util.List;

import com.readup.server.quiz.domain.model.Quiz;
import com.readup.server.quiz.domain.model.QuizOption;
import com.readup.server.quiz.domain.model.QuizSet;

public record CreateQuizSetRequest(Long bookId, Long chapterId, List<CreateQuizRequest> quizRequestList) {

	public QuizSet toEntity() {
		QuizSet quizSet = QuizSet.create(bookId, chapterId);
		quizSet.addQuizList(createQuizList(quizSet));
		return quizSet;
	}

	private List<Quiz> createQuizList(QuizSet quizSet) {
		return quizRequestList.stream()
			.map(quizRequest -> quizRequest.toEntity(quizSet))
			.toList();
	}

	public record CreateQuizRequest(
		String question, String explanation,
		List<CreateQuizOptionRequest> quizOptionRequestList) {

		private Quiz toEntity(QuizSet quizSet) {
			Quiz quiz = Quiz.create(question, explanation, quizSet);
			quiz.addQuizOptionList(createQuizOptionList(quiz));
			return quiz;
		}

		private List<QuizOption> createQuizOptionList(Quiz quiz) {
			return quizOptionRequestList.stream()
				.map(optionRequest -> optionRequest.toEntity(quiz))
				.toList();
		}

		public record CreateQuizOptionRequest(String content, Boolean isCorrect) {

			private QuizOption toEntity(Quiz quiz) {
				return QuizOption.create(content, isCorrect, quiz);
			}
		}
	}
}
