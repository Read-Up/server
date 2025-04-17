package com.readup.server.quiz.application.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.readup.server.quiz.domain.model.Quiz;
import com.readup.server.quiz.domain.model.QuizOption;

public record CreateQuizListResponse(
	Long bookId, Long chapterId,
	List<CreateQuizResponse> quizResponseList) {

	public static CreateQuizListResponse from(Long bookId, Long chapterId, List<Quiz> quizList) {
		return new CreateQuizListResponse(bookId, chapterId, CreateQuizResponse.from(quizList));
	}

	public record CreateQuizResponse(
		String question, String explanation,
		List<CreateQuizOptionResponse> quizOptionResponseList,
		LocalDateTime createAt) {

		public static List<CreateQuizResponse> from(List<Quiz> quizList) {
			return quizList.stream()
				.map(q -> new CreateQuizResponse(q.getQuestion(), q.getExplanation(), CreateQuizOptionResponse.from(q.getQuizOptionList()), q.getCreatedAt()))
				.toList();
		}
	}

	public record CreateQuizOptionResponse(
		Integer number, String content, Boolean isCorrect) {

		public static List<CreateQuizOptionResponse> from(List<QuizOption> quizOptionList) {
			return quizOptionList.stream()
				.map(qo -> new CreateQuizOptionResponse(qo.getNumber(), qo.getContent(), qo.getIsCorrect()))
				.toList();
		}
	}
}
