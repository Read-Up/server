package com.readup.server.quiz.stub;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import com.readup.server.quiz.domain.model.Quiz;
import com.readup.server.quiz.domain.model.QuizSet;
import com.readup.server.quiz.domain.repository.QuizSetRepository;

public class QuizSetJpaRepositoryStub implements QuizSetRepository {

	private final List<QuizSet> quizSetList = new ArrayList<>();
	private final AtomicLong idGenerator = new AtomicLong(1L);

	@Override
	public QuizSet save(QuizSet quizSet) {
		QuizSet quizSetWithId = createQuizSetWithId(quizSet.getBookId(), quizSet.getChapterId(), quizSet.getQuizList());
		quizSetList.add(quizSetWithId);
		return quizSetWithId;
	}

	@Override
	public Optional<QuizSet> findById(Long quizSetId) {
		return quizSetList.stream()
			.filter(quizSet -> quizSet.getId().equals(quizSetId))
			.findFirst();
	}

	private QuizSet createQuizSetWithId(Long bookId, Long chapterId, List<Quiz> quizList) {
		return QuizSet.builder()
			.id(idGenerator.getAndIncrement())
			.bookId(bookId)
			.chapterId(chapterId)
			.quizList(quizList)
			.build();
	}
}
