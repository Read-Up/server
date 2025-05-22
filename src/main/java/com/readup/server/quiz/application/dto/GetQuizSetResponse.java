package com.readup.server.quiz.application.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.readup.server.quiz.domain.model.Quiz;
import com.readup.server.quiz.domain.model.QuizOption;
import com.readup.server.quiz.domain.model.QuizSet;

public record GetQuizSetResponse(Long bookId, Long chapterId, Long quizSetId, LocalDateTime createdAt,
								 List<GetQuizResponse> quizResponseList) {

	public static GetQuizSetResponse from(QuizSet quizSet, int quizSequence) {
		return new GetQuizSetResponse(quizSet.getBookId(), quizSet.getChapterId(), quizSet.getId(),
			quizSet.getCreatedAt(), GetQuizResponse.from(quizSet.getQuizList(), quizSequence));
	}

	public record GetQuizResponse(Long quizId, int sequence, String question,
								  List<GetQuizOptionResponse> quizOptionResponseList) {

		public static List<GetQuizResponse> from(List<Quiz> quizList, int quizSequence) {
			return quizList.stream()
				.filter(q -> q.getSequence() >= quizSequence)
				.map(q -> new GetQuizResponse(q.getId(), q.getSequence(), q.getQuestion(),
					GetQuizOptionResponse.from(q.getQuizOptionList())))
				.toList();
		}
	}

	public record GetQuizOptionResponse(Long quizOptionId, int sequence, String content) {

		public static List<GetQuizOptionResponse> from(List<QuizOption> quizOptionList) {
			return quizOptionList.stream()
				.map(qo -> new GetQuizOptionResponse(qo.getId(), qo.getSequence(), qo.getContent()))
				.toList();
		}
	}
}
