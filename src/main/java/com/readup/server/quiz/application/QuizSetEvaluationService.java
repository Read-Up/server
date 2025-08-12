package com.readup.server.quiz.application;

import static com.readup.server.common.exception.ErrorCode.*;

import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.readup.server.common.exception.ServiceException;
import com.readup.server.quiz.domain.model.QuizSet;
import com.readup.server.quiz.domain.repository.QuizSetRepository;
import com.readup.server.user_quiz.event.QuizSetEvaluatedEvent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class QuizSetEvaluationService {

	private final QuizSetRepository quizSetRepository;

	@Transactional
	@Retryable(
		retryFor = OptimisticLockingFailureException.class,
		maxAttempts = 5,
		backoff = @Backoff(delay = 100, multiplier = 1.5)
	)
	public void applyUserEvaluation(QuizSetEvaluatedEvent event) {
		QuizSet quizSet = quizSetRepository.getQuizSetById(event.quizSetId());
		quizSet.applyNewEvaluation(event.correctAnswerAverage(), event.likeScore());
	}

	@Recover
	public void recoverApplyUserEvaluation(OptimisticLockingFailureException e, QuizSetEvaluatedEvent event) {
		log.error("QuizSet evaluation failed after retries. userQuizSetId: {}, quizSetId: {}",
			event.userQuizSetId(), event.quizSetId(), e);
		throw new ServiceException(OPTIMISTIC_LOCKING_FAILURE);
	}
}
