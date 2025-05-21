package com.readup.server.user_quiz.application;

import static com.readup.server.common.exception.ErrorCode.*;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.readup.server.common.exception.ServiceException;
import com.readup.server.quiz.domain.model.Quiz;
import com.readup.server.quiz.domain.model.QuizSet;
import com.readup.server.quiz.domain.repository.QuizSetRepository;
import com.readup.server.user.dto.AuthUser;
import com.readup.server.user_quiz.application.dto.GetUserQuizSetResponse;
import com.readup.server.user_quiz.domain.model.UserQuizSet;
import com.readup.server.user_quiz.domain.repository.UserQuizSetRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserQuizSetService {

	private final QuizSetRepository quizSetRepository;
	private final UserQuizSetRepository userQuizSetRepository;

	@Transactional
	public GetUserQuizSetResponse getUserQuizSet(Long quizSetId, AuthUser user) {
		QuizSet quizSet = getQuizSet(quizSetId);
		UserQuizSet userQuizSet = getOrCreateUserQuizSet(quizSetId, user.id(), quizSet.getQuizList());
		return GetUserQuizSetResponse.from(userQuizSet);
	}

	private UserQuizSet getOrCreateUserQuizSet(Long quizSetId, Long userId, List<Quiz> quizList) {
		return userQuizSetRepository.findByQuizSetIdAndCreatedBy(quizSetId, userId)
			.orElseGet(() -> createNewUserQuizSet(quizSetId, quizList));
	}

	private UserQuizSet createNewUserQuizSet(Long quizSetId, List<Quiz> quizList) {
		List<Long> quizIdList = quizList.stream().map(Quiz::getId).toList();
		UserQuizSet newUserQuizSet = UserQuizSet.create(quizSetId, quizIdList);
		return userQuizSetRepository.save(newUserQuizSet);
	}

	private QuizSet getQuizSet(Long quizSetId) {
		return quizSetRepository.findById(quizSetId)
			.orElseThrow(() -> new ServiceException(NOT_FOUND_QUIZ_SET));
	}
}
