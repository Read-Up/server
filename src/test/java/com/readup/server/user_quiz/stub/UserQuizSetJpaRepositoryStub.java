package com.readup.server.user_quiz.stub;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.test.util.ReflectionTestUtils;

import com.readup.server.user_quiz.domain.model.UserQuizSet;
import com.readup.server.user_quiz.domain.repository.UserQuizSetRepository;

public class UserQuizSetJpaRepositoryStub implements UserQuizSetRepository {

	private final List<UserQuizSet> userQuizSetList = new ArrayList<>();
	private final AtomicLong idGenerator = new AtomicLong(1L);

	public int size() {
		return userQuizSetList.size();
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
			.filter(uqs -> uqs.getQuizSetId().equals(quizSetId) && uqs.getCreatedBy().equals(userId))
			.findFirst();
	}

	private UserQuizSet createUserQuizSetWithId(UserQuizSet userQuizSet) {
		UserQuizSet userQuizSetWithId = UserQuizSet.builder()
			.id(idGenerator.getAndIncrement())
			.quizSetId(userQuizSet.getQuizSetId())
			.isEvaluated(userQuizSet.getIsEvaluated())
			.quizSequence(userQuizSet.getQuizSequence())
			.userQuizList(userQuizSet.getUserQuizList())
			.build();
		ReflectionTestUtils.setField(userQuizSetWithId, "createdBy", userQuizSet.getCreatedBy());
		return userQuizSetWithId;
	}
}
