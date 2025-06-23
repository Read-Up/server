package com.readup.server.quiz.application.dto;

import static com.readup.server.quiz.util.SequenceGenerator.*;

import java.time.LocalDateTime;
import java.util.List;

import com.readup.server.quiz.domain.model.Quiz;
import com.readup.server.quiz.domain.model.QuizOption;
import com.readup.server.quiz.domain.model.QuizSet;

public record CreateQuizSetResponse(
	Long bookId, Long chapterId, Long quizSetId, int estimatedTime, int totalQuizCount,
	LocalDateTime createdAt, List<CreateQuizResponse> quizResponseList) {

	public static CreateQuizSetResponse from(Long bookId, QuizSet quizSet) {
		return new CreateQuizSetResponse(
			bookId,
			quizSet.getChapterId(),
			quizSet.getId(),
			quizSet.getEstimatedTime(),
			quizSet.getTotalQuizCount(),
			quizSet.getCreatedAt(),
			CreateQuizResponse.from(quizSet.getQuizList()));
	}

	public record CreateQuizResponse(
		Long quizId, int sequence, String question, String explanation,
		List<CreateQuizOptionResponse> quizOptionResponseList) {

		private static List<CreateQuizResponse> from(List<Quiz> quizList) {
			return createResponsesWithSequence(quizList, CreateQuizResponse::createWithSequence);
		}

		private static CreateQuizResponse createWithSequence(Quiz quiz, int sequence) {
			return new CreateQuizResponse(
				quiz.getId(),
				sequence,
				quiz.getQuestion(),
				quiz.getExplanation(),
				CreateQuizOptionResponse.from(quiz.getQuizOptionList()));
		}
	}

	public record CreateQuizOptionResponse(Long quizOptionId, int sequence, String content, Boolean isCorrect) {

		private static List<CreateQuizOptionResponse> from(List<QuizOption> quizOptionList) {
			return createResponsesWithSequence(quizOptionList, CreateQuizOptionResponse::createWithSequence);
		}

		private static CreateQuizOptionResponse createWithSequence(QuizOption quizOption, int sequence) {
			return new CreateQuizOptionResponse(
				quizOption.getId(),
				sequence,
				quizOption.getContent(),
				quizOption.getIsCorrect());
		}
	}
}
