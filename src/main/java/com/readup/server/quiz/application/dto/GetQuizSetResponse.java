package com.readup.server.quiz.application.dto;

import static com.readup.server.quiz.util.SequenceGenerator.*;

import java.time.LocalDateTime;
import java.util.List;

import com.readup.server.quiz.domain.model.Quiz;
import com.readup.server.quiz.domain.model.QuizOption;
import com.readup.server.quiz.domain.model.QuizSet;

public record GetQuizSetResponse(Long bookId, Long chapterId, Long quizSetId, LocalDateTime createdAt,
								 List<GetQuizResponse> quizResponseList) {

	public static GetQuizSetResponse from(QuizSet quizSet, Long lastQuizId) {
		return new GetQuizSetResponse(
			quizSet.getBookId(),
			quizSet.getChapterId(),
			quizSet.getId(),
			quizSet.getCreatedAt(),
			GetQuizResponse.from(quizSet.getQuizList(), lastQuizId));
	}

	public record GetQuizResponse(Long quizId, int sequence, String question,
								  List<GetQuizOptionResponse> quizOptionResponseList) {

		public static List<GetQuizResponse> from(List<Quiz> quizList, Long lastQuizId) {
			List<Quiz> filteredQuizList = filterQuizById(quizList, lastQuizId);
			return createResponsesWithSequence(filteredQuizList, GetQuizResponse::createWithSequence);
		}

		private static List<Quiz> filterQuizById(List<Quiz> quizList, Long lastQuizId) {
			return quizList.stream()
				.filter(q -> q.getId() > lastQuizId)
				.toList();
		}

		private static GetQuizResponse createWithSequence(Quiz quiz, int sequence) {
			return new GetQuizResponse(
				quiz.getId(),
				sequence,
				quiz.getQuestion(),
				GetQuizOptionResponse.from(quiz.getQuizOptionList()));
		}
	}

	public record GetQuizOptionResponse(Long quizOptionId, int sequence, String content) {

		public static List<GetQuizOptionResponse> from(List<QuizOption> quizOptionList) {
			return createResponsesWithSequence(quizOptionList, GetQuizOptionResponse::createWithSequence);
		}

		private static GetQuizOptionResponse createWithSequence(QuizOption quizOption, int sequence) {
			return new GetQuizOptionResponse(
				quizOption.getId(),
				sequence,
				quizOption.getContent());
		}
	}
}
