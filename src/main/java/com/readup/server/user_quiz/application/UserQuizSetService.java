package com.readup.server.user_quiz.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.readup.server.quiz.domain.model.Quiz;
import com.readup.server.quiz.domain.model.QuizSet;
import com.readup.server.quiz.domain.repository.QuizQueryRepository;
import com.readup.server.quiz.domain.repository.QuizSetRepository;
import com.readup.server.user.dto.AuthUser;
import com.readup.server.user_quiz.application.dto.GetUserQuizSetResponse;
import com.readup.server.user_quiz.application.dto.SubmitUserQuizRequest;
import com.readup.server.user_quiz.application.dto.SubmitUserQuizResponse;
import com.readup.server.user_quiz.domain.model.UserQuiz;
import com.readup.server.user_quiz.domain.model.UserQuizSet;
import com.readup.server.user_quiz.domain.repository.UserQuizSetRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserQuizSetService {

	private final QuizSetRepository quizSetRepository;
	private final QuizQueryRepository quizQueryRepository;
	private final UserQuizSetRepository userQuizSetRepository;

	@Transactional
	public GetUserQuizSetResponse getUserQuizSet(Long quizSetId, AuthUser user) {
		QuizSet quizSet = quizSetRepository.getQuizSetById(quizSetId);
		UserQuizSet userQuizSet = getOrCreateUserQuizSet(quizSetId, user.id(), quizSet.getQuizList());
		return GetUserQuizSetResponse.from(userQuizSet);
	}

	@Transactional
	public SubmitUserQuizResponse submitUserQuizAnswer(Long quizSetId, Long quizId, SubmitUserQuizRequest request,
		AuthUser user) {
		Quiz quiz = quizQueryRepository.getQuizWithQuizOptionById(quizSetId, quizId);
		UserQuiz userQuiz = getUserQuiz(quizSetId, quizId, user);

		boolean isAnswerCorrect = quiz.isAnswerCorrect(request.selectedQuizOptionSequences());
		userQuiz.submitUserQuiz(isAnswerCorrect);

		return SubmitUserQuizResponse.of(isAnswerCorrect, quiz.getExplanation());
	}

	private UserQuiz getUserQuiz(Long quizSetId, Long quizId, AuthUser user) {
		return userQuizSetRepository.getUserQuizSetWithUserQuizById(quizSetId, quizId, user.id())
			.getUserQuizList().getFirst();
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
}
