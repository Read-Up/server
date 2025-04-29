package com.readup.server.quiz.application.dto;

import java.util.List;

import com.readup.server.quiz.domain.model.Quiz;
import com.readup.server.quiz.domain.model.QuizOption;
import com.readup.server.quiz.domain.model.QuizSet;

public record GetQuizSetResponse(Long bookId, Long chapterId, Long quizSetId,
								 List<GetQuizResponse> quizResponseList) {

	public static GetQuizSetResponse from(QuizSet quizSet) {
		return new GetQuizSetResponse(quizSet.getBookId(), quizSet.getChapterId(), quizSet.getId(),
			GetQuizResponse.from(quizSet.getQuizList()));
	}

	public record GetQuizResponse(String question, List<GetQuizOptionResponse> quizOptionResponseList) {

		public static List<GetQuizResponse> from(List<Quiz> quizList) {
			return quizList.stream()
				.map(q -> new GetQuizResponse(q.getQuestion(), GetQuizOptionResponse.from(q.getQuizOptionList())))
				.toList();
		}
	}

	public record GetQuizOptionResponse(String content) {

		public static List<GetQuizOptionResponse> from(List<QuizOption> quizOptionList) {
			return quizOptionList.stream()
				.map(qo -> new GetQuizOptionResponse(qo.getContent()))
				.toList();
		}
	}
}
