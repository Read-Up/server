package com.readup.server.quiz.stub;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import com.readup.server.quiz.domain.model.Quiz;
import com.readup.server.quiz.domain.repository.QuizRepository;

public class QuizJpaRepositoryStub implements QuizRepository {

	private final List<Quiz> quizList = new ArrayList<>();
	private final AtomicLong idGenerator = new AtomicLong(1L);

	@Override
	@SuppressWarnings("unchecked")
	public <S extends Quiz> List<S> saveAll(Iterable<S> quizzes) {
		List<Quiz> quizWithIdList = createQuizWithId((List<Quiz>) quizzes);

		quizList.addAll(quizWithIdList);

		return (List<S>) quizWithIdList;
	}

	private List<Quiz> createQuizWithId(List<Quiz> quizzes) {
		return quizzes.stream()
			.map(quiz -> Quiz.builder()
					.id(idGenerator.getAndIncrement())
					.chapter(quiz.getChapter())
					.question(quiz.getQuestion())
					.explanation(quiz.getExplanation())
					.answerOptionNumber(quiz.getAnswerOptionNumber())
					.quizOptionList(quiz.getQuizOptionList())
					.build())
			.toList();
	}
}
