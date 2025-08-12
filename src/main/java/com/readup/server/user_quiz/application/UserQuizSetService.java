package com.readup.server.user_quiz.application;

import static com.readup.server.common.exception.ErrorCode.*;
import static com.readup.server.user_quiz.domain.model.UserQuizSetStatus.*;
import static java.lang.Boolean.*;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.readup.server.common.exception.ServiceException;
import com.readup.server.quiz.domain.model.Quiz;
import com.readup.server.quiz.domain.model.QuizSet;
import com.readup.server.quiz.domain.repository.QuizQueryRepository;
import com.readup.server.quiz.domain.repository.QuizSetRepository;
import com.readup.server.user_quiz.application.dto.CompleteUserQuizSetResponse;
import com.readup.server.user_quiz.application.dto.GetUserQuizSetResponse;
import com.readup.server.user_quiz.application.dto.GetUserQuizSetResultResponse;
import com.readup.server.user_quiz.application.dto.ResetUserQuizSetResponse;
import com.readup.server.user_quiz.application.dto.SubmitUserQuizRequest;
import com.readup.server.user_quiz.application.dto.SubmitUserQuizResponse;
import com.readup.server.user_quiz.domain.model.UserQuiz;
import com.readup.server.user_quiz.domain.model.UserQuizSet;
import com.readup.server.user_quiz.domain.repository.UserQuizSetRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserQuizSetService {

	private static final double HALF_CORRECT_THRESHOLD = 0.5;

	private final QuizSetRepository quizSetRepository;
	private final QuizQueryRepository quizQueryRepository;
	private final UserQuizSetRepository userQuizSetRepository;

	@Transactional
	public GetUserQuizSetResponse getUserQuizSet(Long quizSetId, Long socialAccountId) {
		QuizSet quizSet = quizSetRepository.getQuizSetById(quizSetId);
		UserQuizSet userQuizSet = getOrCreateUserQuizSet(quizSetId, socialAccountId, getQuizIdList(quizSet));
		return GetUserQuizSetResponse.from(userQuizSet);
	}

	@Transactional(readOnly = true)
	public GetUserQuizSetResultResponse getUserQuizSetResult(Long userQuizSetId, Long socialAccountId) {
		UserQuizSet userQuizSet = userQuizSetRepository.getWithUserQuizById(userQuizSetId, socialAccountId);
		return calculateQuizResult(userQuizSet);
	}

	@Transactional
	public ResetUserQuizSetResponse resetUserQuizSet(Long userQuizSetId, Long socialAccountId) {
		UserQuizSet userQuizSet = userQuizSetRepository.getByIdAndCreatedBy(userQuizSetId, socialAccountId);
		userQuizSet.reset();
		return ResetUserQuizSetResponse.of(userQuizSet.getId(), userQuizSet.getQuizSetId());
	}

	@Transactional
	public CompleteUserQuizSetResponse completeUserQuizSet(Long userQuizSetId, Long socialAccountId) {
		UserQuizSet userQuizSet = userQuizSetRepository.getByIdAndCreatedBy(userQuizSetId, socialAccountId);
		userQuizSet.complete();
		return CompleteUserQuizSetResponse.of(userQuizSet.getId(), userQuizSet.getQuizSetId());
	}

	@Transactional
	public SubmitUserQuizResponse submitUserQuizAnswer(Long quizSetId, Long quizId, SubmitUserQuizRequest request,
		Long socialAccountId) {
		Quiz quiz = quizQueryRepository.getWithQuizOptionById(quizSetId, quizId);
		UserQuizSet userQuizSet = userQuizSetRepository.getWithUserQuizByQuizSetId(quizSetId, socialAccountId);
		UserQuiz userQuiz = userQuizSet.findUserQuizByQuizId(quizId);

		userQuizSet.updateLastQuizId(quizId);
		boolean isAnswerCorrect = userQuiz.submitAnswer(quiz, request.selectedQuizOptionIds());

		return SubmitUserQuizResponse.of(isAnswerCorrect, quiz.getExplanation());
	}

	private List<Long> getQuizIdList(QuizSet quizSet) {
		return quizSet.getQuizList().stream()
			.map(Quiz::getId)
			.toList();
	}

	private UserQuizSet getOrCreateUserQuizSet(Long quizSetId, Long socialAccountId, List<Long> quizIdList) {
		return userQuizSetRepository.findByQuizSetIdAndCreatedBy(quizSetId, socialAccountId)
			.orElseGet(() -> createNewUserQuizSet(quizSetId, quizIdList));
	}

	private UserQuizSet createNewUserQuizSet(Long quizSetId, List<Long> quizIdList) {
		UserQuizSet newUserQuizSet = UserQuizSet.create(quizSetId);
		newUserQuizSet.addUserQuizList(createUserQuizList(quizIdList, newUserQuizSet));
		return userQuizSetRepository.save(newUserQuizSet);
	}

	private List<UserQuiz> createUserQuizList(List<Long> quizIdList, UserQuizSet newUserQuizSet) {
		return quizIdList.stream()
			.map(qi -> UserQuiz.create(qi, newUserQuizSet))
			.toList();
	}

	private GetUserQuizSetResultResponse calculateQuizResult(UserQuizSet userQuizSet) {
		validateUserQuizSet(userQuizSet);
		List<UserQuiz> userQuizList = userQuizSet.getUserQuizList();
		int solvedCount = userQuizList.size();
		int firstAttemptCorrect = 0;
		int retryCorrect = 0;

		for (UserQuiz quiz : userQuizList) {
			boolean currentCorrect = TRUE.equals(quiz.getCurrentAttemptCorrect());
			boolean firstCorrect = TRUE.equals(quiz.getFirstAttemptCorrect());

			if (currentCorrect) {
				if (firstCorrect) {
					firstAttemptCorrect++;
				} else {
					retryCorrect++;
				}
			}
		}

		boolean isAboveHalf = calculateIsAboveHalfCorrect(solvedCount, firstAttemptCorrect);

		return new GetUserQuizSetResultResponse(solvedCount, firstAttemptCorrect, retryCorrect, isAboveHalf);
	}

	private boolean calculateIsAboveHalfCorrect(int solvedCount, int firstAttemptCorrect) {
		return solvedCount > 0 && (double)firstAttemptCorrect / solvedCount >= HALF_CORRECT_THRESHOLD;
	}

	private void validateUserQuizSet(UserQuizSet userQuizSet) {
		if (userQuizSet.getStatus() != COMPLETED) {
			throw new ServiceException(NOT_COMPLETE_USER_QUIZ_SET);
		}
	}
}
