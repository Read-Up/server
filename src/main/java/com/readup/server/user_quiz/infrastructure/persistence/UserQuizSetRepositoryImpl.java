package com.readup.server.user_quiz.infrastructure.persistence;

import static com.readup.server.common.exception.ErrorCode.*;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.readup.server.common.exception.RepositoryException;
import com.readup.server.user_quiz.domain.model.UserQuizSet;
import com.readup.server.user_quiz.domain.repository.UserQuizSetRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class UserQuizSetRepositoryImpl implements UserQuizSetRepository {

	private final UserQuizSetJpaRepository userQuizSetJpaRepository;

	@Override
	public UserQuizSet save(UserQuizSet userQuizSet) {
		return userQuizSetJpaRepository.save(userQuizSet);
	}

	@Override
	public Optional<UserQuizSet> findByQuizSetIdAndCreatedBy(Long quizSetId, Long socialAccountId) {
		return userQuizSetJpaRepository.findByQuizSetIdAndCreatedBy(quizSetId, socialAccountId);
	}

	@Override
	public UserQuizSet getByIdAndCreatedBy(Long userQuizSetId, Long socialAccountId) {
		return userQuizSetJpaRepository.findByIdAndCreatedBy(userQuizSetId, socialAccountId)
			.orElseThrow(() -> new RepositoryException(NOT_FOUND_USER_QUIZ_SET));
	}

	@Override
	public UserQuizSet getWithUserQuizByQuizSetId(Long quizSetId, Long socialAccountId) {
		return userQuizSetJpaRepository.findWithUserQuizByQuizSetId(quizSetId, socialAccountId)
			.orElseThrow(() -> new RepositoryException(NOT_FOUND_USER_QUIZ_SET));
	}

	@Override
	public UserQuizSet getWithUserQuizById(Long userQuizSetId, Long socialAccountId) {
		return userQuizSetJpaRepository.findWithUserQuizById(userQuizSetId, socialAccountId)
			.orElseThrow(() -> new RepositoryException(NOT_FOUND_USER_QUIZ_SET));
	}
}
