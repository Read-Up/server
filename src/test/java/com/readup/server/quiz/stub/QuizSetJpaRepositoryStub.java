package com.readup.server.quiz.stub;

import static com.readup.server.common.exception.ErrorCode.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.test.util.ReflectionTestUtils;

import com.readup.server.common.exception.RepositoryException;
import com.readup.server.quiz.domain.model.QuizSet;
import com.readup.server.quiz.domain.repository.QuizSetRepository;

public class QuizSetJpaRepositoryStub implements QuizSetRepository {

	private final List<QuizSet> quizSetList = new ArrayList<>();
	private final AtomicLong idGenerator = new AtomicLong(1L);

	@Override
	public QuizSet save(QuizSet quizSet) {
		QuizSet quizSetWithId = createQuizSetWithId(quizSet);
		quizSetList.add(quizSetWithId);
		return quizSetWithId;
	}

	@Override
	public QuizSet getQuizSetById(Long quizSetId) {
		return quizSetList.stream()
			.filter(qs -> qs.getId().equals(quizSetId))
			.findFirst()
			.orElseThrow(() -> new RepositoryException(NOT_FOUND_QUIZ_SET));
	}

	private QuizSet createQuizSetWithId(QuizSet quizSet) {
		ReflectionTestUtils.setField(quizSet, "id", idGenerator.getAndIncrement());
		ReflectionTestUtils.setField(quizSet, "createdBy", quizSet.getCreatedBy());
		return quizSet;
	}
}