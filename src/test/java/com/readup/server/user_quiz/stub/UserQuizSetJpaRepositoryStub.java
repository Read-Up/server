package com.readup.server.user_quiz.stub;

import static com.readup.server.common.exception.ErrorCode.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.test.util.ReflectionTestUtils;

import com.readup.server.common.exception.RepositoryException;
import com.readup.server.user_quiz.domain.model.UserQuizSet;
import com.readup.server.user_quiz.domain.repository.UserQuizSetRepository;

public class UserQuizSetJpaRepositoryStub implements UserQuizSetRepository {

	private final List<UserQuizSet> userQuizSetList = new ArrayList<>();
	private final AtomicLong idGenerator = new AtomicLong(1L);

	public long getCurrentId() {
		return idGenerator.get() - 1;
	}

	public int size() {
		return userQuizSetList.size();
	}

	public void clear() {
		userQuizSetList.clear();
		idGenerator.set(1L);
	}

	@Override
	public UserQuizSet save(UserQuizSet userQuizSet) {
		UserQuizSet userQuizSetWithId = createUserQuizSetWithId(userQuizSet);
		userQuizSetList.add(userQuizSetWithId);
		return userQuizSetWithId;
	}

	@Override
	public Optional<UserQuizSet> findByQuizSetIdAndCreatedBy(Long quizSetId, Long userId) {
		return userQuizSetList.stream()
			.filter(uqs -> uqs.getQuizSetId().equals(quizSetId))
			.filter(uqs -> uqs.getCreatedBy().equals(userId))
			.findFirst();
	}

	@Override
	public UserQuizSet getUserQuizSetWithUserQuizById(Long quizSetId, Long quizId, Long userId) {
		return userQuizSetList.stream()
			.filter(uqs -> uqs.getQuizSetId().equals(quizSetId))
			.filter(uqs -> uqs.getCreatedBy().equals(userId))
			.filter(uqs -> uqs.getUserQuizList().stream()
				.anyMatch(userQuiz -> userQuiz.getQuizId().equals(quizId)))
			.findFirst()
			.orElseThrow(() -> new RepositoryException(NOT_FOUND_USER_QUIZ_SET));
	}

	private UserQuizSet createUserQuizSetWithId(UserQuizSet userQuizSet) {
		ReflectionTestUtils.setField(userQuizSet, "id", idGenerator.getAndIncrement());
		AtomicLong userQuizIdGenerator = new AtomicLong(1L);
		userQuizSet.getUserQuizList().forEach(uq ->
			ReflectionTestUtils.setField(uq, "id", userQuizIdGenerator.getAndIncrement())
		);
		return userQuizSet;
	}
}