package com.readup.server.quiz.application.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.readup.server.quiz.domain.model.Quiz;
import com.readup.server.quiz.domain.model.QuizOption;
import com.readup.server.quiz.domain.model.QuizSet;

public record CreateQuizSetResponse(
	Long bookId, Long chapterId, Long quizSetId,
	LocalDateTime createAt, List<CreateQuizResponse> quizResponseList) {

	public static CreateQuizSetResponse from(Long bookId, QuizSet quizSet) {
		return new CreateQuizSetResponse(bookId, quizSet.getChapterId(), quizSet.getId(), quizSet.getCreatedAt(),
			CreateQuizResponse.from(quizSet.getQuizList()));
	}

	public record CreateQuizResponse(
		String question, String explanation,
		List<CreateQuizOptionResponse> quizOptionResponseList) {

		public static List<CreateQuizResponse> from(List<Quiz> quizList) {
			return quizList.stream()
				.map(q -> new CreateQuizResponse(q.getQuestion(), q.getExplanation(),
					CreateQuizOptionResponse.from(q.getQuizOptionList())))
				.toList();
		}
	}

	public record CreateQuizOptionResponse(String content, Boolean isCorrect) {

		public static List<CreateQuizOptionResponse> from(List<QuizOption> quizOptionList) {
			return quizOptionList.stream()
				.map(qo -> new CreateQuizOptionResponse(qo.getContent(), qo.getIsCorrect()))
				.toList();
		}
	}
}
