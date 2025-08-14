package com.readup.server.user_quiz.application;

import static com.readup.server.common.exception.ErrorCode.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import com.readup.server.common.exception.ServiceException;
import com.readup.server.quiz.domain.model.Quiz;
import com.readup.server.quiz.domain.model.QuizOption;
import com.readup.server.quiz.domain.model.QuizSet;
import com.readup.server.quiz.domain.repository.QuizQueryRepository;
import com.readup.server.quiz.domain.repository.QuizSetRepository;
import com.readup.server.user_quiz.application.dto.CompleteUserQuizSetResponse;
import com.readup.server.user_quiz.application.dto.EvaluateQuizSetRequest;
import com.readup.server.user_quiz.application.dto.EvaluateQuizSetResponse;
import com.readup.server.user_quiz.application.dto.GetUserQuizSetResponse;
import com.readup.server.user_quiz.application.dto.GetUserQuizSetResultResponse;
import com.readup.server.user_quiz.application.dto.ResetUserQuizSetResponse;
import com.readup.server.user_quiz.application.dto.SubmitUserQuizRequest;
import com.readup.server.user_quiz.application.dto.SubmitUserQuizResponse;
import com.readup.server.user_quiz.domain.model.UserQuiz;
import com.readup.server.user_quiz.domain.model.UserQuizSet;
import com.readup.server.user_quiz.domain.repository.UserQuizSetRepository;

@ExtendWith(MockitoExtension.class)
class UserQuizSetServiceTest {

	@InjectMocks
	private UserQuizSetService sut;

	@Mock
	private QuizSetRepository quizSetRepository;

	@Mock
	private QuizQueryRepository quizQueryRepository;

	@Mock
	private UserQuizSetRepository userQuizSetRepository;

	private static final Long EXPECTED_QUIZ_SET_ID = 1L;
	private static final Long EXPECTED_USER_QUIZ_SET_ID = 1L;
	private static final Long EXPECTED_SOCIAL_ACCOUNT_ID = 100L;
	private static final Long EXPECTED_QUIZ_ID_1 = 1L;
	private static final Long EXPECTED_QUIZ_ID_2 = 2L;

	@Nested
	class GetUserQuizSet {

		@Test
		@DisplayName("사용자 퀴즈 세트 조회 성공 - 사용자 기존 퀴즈 세트 존재")
		void get_user_quiz_set_success_existing_user_quiz_set() {
			// given
			UserQuizSet existingUserQuizSet = createExistingUserQuizSet();

			// stubbing
			when(quizSetRepository.getQuizSetById(EXPECTED_QUIZ_SET_ID)).thenReturn(createQuizSet());
			when(userQuizSetRepository.findByQuizSetIdAndCreatedBy(EXPECTED_QUIZ_SET_ID, EXPECTED_SOCIAL_ACCOUNT_ID))
				.thenReturn(Optional.of(existingUserQuizSet));

			// when
			GetUserQuizSetResponse response = sut.getUserQuizSet(EXPECTED_QUIZ_SET_ID, EXPECTED_SOCIAL_ACCOUNT_ID);

			// then
			assertThat(response).isNotNull();
			assertThat(response.userQuizSetId()).isEqualTo(existingUserQuizSet.getId());
			assertThat(response.lastQuizId()).isEqualTo(existingUserQuizSet.getLastQuizId());
			assertThat(response.isEvaluated()).isEqualTo(existingUserQuizSet.getIsEvaluated());

			verify(quizSetRepository, times(1)).getQuizSetById(EXPECTED_QUIZ_SET_ID);
			verify(userQuizSetRepository, times(1)).findByQuizSetIdAndCreatedBy(EXPECTED_QUIZ_SET_ID,
				EXPECTED_SOCIAL_ACCOUNT_ID);
		}

		@Test
		@DisplayName("사용자 퀴즈 세트 조회 성공 - 처음으로 퀴즈 세트 푸는 경우 사용자 퀴즈 세트 생성")
		void get_user_quiz_set_success_new_user_quiz_set() {
			// given
			UserQuizSet newUserQuizSet = createNewUserQuizSet();

			// stubbing
			when(quizSetRepository.getQuizSetById(EXPECTED_QUIZ_SET_ID)).thenReturn(createQuizSet());
			when(userQuizSetRepository.findByQuizSetIdAndCreatedBy(EXPECTED_QUIZ_SET_ID, EXPECTED_SOCIAL_ACCOUNT_ID))
				.thenReturn(Optional.empty());
			when(userQuizSetRepository.save(any(UserQuizSet.class))).thenReturn(newUserQuizSet);

			// when
			GetUserQuizSetResponse response = sut.getUserQuizSet(EXPECTED_QUIZ_SET_ID, EXPECTED_SOCIAL_ACCOUNT_ID);

			// then
			assertThat(response).isNotNull();
			assertThat(response.userQuizSetId()).isEqualTo(newUserQuizSet.getId());
			assertThat(response.lastQuizId()).isNull();
			assertThat(response.isEvaluated()).isFalse();

			verify(quizSetRepository, times(1)).getQuizSetById(EXPECTED_QUIZ_SET_ID);
			verify(userQuizSetRepository, times(1)).findByQuizSetIdAndCreatedBy(EXPECTED_QUIZ_SET_ID,
				EXPECTED_SOCIAL_ACCOUNT_ID);
			verify(userQuizSetRepository, times(1)).save(any(UserQuizSet.class));
		}

		private UserQuizSet createExistingUserQuizSet() {
			UserQuizSet userQuizSet = UserQuizSet.create(EXPECTED_QUIZ_SET_ID);
			ReflectionTestUtils.setField(userQuizSet, "id", 1L);
			ReflectionTestUtils.setField(userQuizSet, "createdBy", EXPECTED_SOCIAL_ACCOUNT_ID);

			List<UserQuiz> userQuizList = List.of(
				UserQuiz.create(EXPECTED_QUIZ_ID_1, userQuizSet),
				UserQuiz.create(EXPECTED_QUIZ_ID_2, userQuizSet)
			);

			userQuizSet.addUserQuizList(userQuizList);

			return userQuizSet;
		}

		private UserQuizSet createNewUserQuizSet() {
			UserQuizSet userQuizSet = UserQuizSet.create(EXPECTED_QUIZ_SET_ID);
			ReflectionTestUtils.setField(userQuizSet, "id", 1L);
			ReflectionTestUtils.setField(userQuizSet, "createdBy", EXPECTED_SOCIAL_ACCOUNT_ID);
			return userQuizSet;
		}
	}

	@Nested
	class GetUserQuizSetResult {

		@Test
		@DisplayName("유저 퀴즈 세트 결과 조회 성공")
		void get_complete_user_quiz_set_result_success() {
			// given
			UserQuizSet userQuizSet = createUserQuizSet();
			userQuizSet.complete();

			// stubbing
			when(userQuizSetRepository.getWithUserQuizById(EXPECTED_USER_QUIZ_SET_ID, EXPECTED_SOCIAL_ACCOUNT_ID))
				.thenReturn(userQuizSet);

			// when
			GetUserQuizSetResultResponse response = sut.getUserQuizSetResult(EXPECTED_USER_QUIZ_SET_ID,
				EXPECTED_SOCIAL_ACCOUNT_ID);

			// then
			verify(userQuizSetRepository, times(1)).getWithUserQuizById(EXPECTED_USER_QUIZ_SET_ID,
				EXPECTED_SOCIAL_ACCOUNT_ID);
			assertThat(response).isNotNull();
		}

		@Test
		@DisplayName("유저 퀴즈 세트 결과 조회 실패 : 완료되지 않은 유저 퀴즈 세트 결과 조회 시 실패")
		void get_incomplete_user_quiz_set_result_fail() {
			// given
			UserQuizSet userQuizSet = createUserQuizSet();

			// stubbing
			when(userQuizSetRepository.getWithUserQuizById(EXPECTED_USER_QUIZ_SET_ID, EXPECTED_SOCIAL_ACCOUNT_ID))
				.thenReturn(userQuizSet);

			// when && then
			assertThatThrownBy(() -> sut.getUserQuizSetResult(EXPECTED_USER_QUIZ_SET_ID, EXPECTED_SOCIAL_ACCOUNT_ID))
				.isInstanceOf(ServiceException.class)
				.hasMessage(NOT_COMPLETE_USER_QUIZ_SET.getMessage());
		}
	}

	@Nested
	class StartUserQuizSet {

		@Test
		@DisplayName("유저 퀴즈 세트 시작 성공")
		void start_user_quiz_set_success() {
			// given
			final UserQuizSet userQuizSet = createUserQuizSet();

			// stubbing
			when(userQuizSetRepository.getByIdAndCreatedBy(EXPECTED_USER_QUIZ_SET_ID, EXPECTED_SOCIAL_ACCOUNT_ID))
				.thenReturn(userQuizSet);

			// when
			ResetUserQuizSetResponse response = sut.resetUserQuizSet(EXPECTED_USER_QUIZ_SET_ID,
				EXPECTED_SOCIAL_ACCOUNT_ID);

			// then
			verify(userQuizSetRepository, times(1)).getByIdAndCreatedBy(EXPECTED_USER_QUIZ_SET_ID,
				EXPECTED_SOCIAL_ACCOUNT_ID);
			assertThat(response).isNotNull();
		}
	}

	@Nested
	class CompleteUserQuizSet {

		@Test
		@DisplayName("유저 퀴즈 세트 완료 성공")
		void complete_user_quiz_set_success() {
			// given
			final UserQuizSet userQuizSet = createUserQuizSet();

			// stubbing
			when(userQuizSetRepository.getByIdAndCreatedBy(EXPECTED_USER_QUIZ_SET_ID, EXPECTED_SOCIAL_ACCOUNT_ID))
				.thenReturn(userQuizSet);

			// when
			CompleteUserQuizSetResponse response = sut.completeUserQuizSet(EXPECTED_USER_QUIZ_SET_ID,
				EXPECTED_SOCIAL_ACCOUNT_ID);

			// then
			verify(userQuizSetRepository, times(1)).getByIdAndCreatedBy(EXPECTED_USER_QUIZ_SET_ID,
				EXPECTED_SOCIAL_ACCOUNT_ID);
			assertThat(response).isNotNull();

		}
	}

	@Nested
	class SubmitUserQuizAnswer {

		@Test
		@DisplayName("퀴즈 답안 제출 성공 - 정답인 경우")
		void submit_user_quiz_answer_success_correct() {
			// given
			Set<Long> correctAnswerIds = Set.of(1L);
			SubmitUserQuizRequest request = new SubmitUserQuizRequest(correctAnswerIds);
			UserQuizSet userQuizSet = createUserQuizSetWithUserQuiz();

			// stubbing
			when(quizQueryRepository.getWithQuizOptionById(EXPECTED_QUIZ_SET_ID, EXPECTED_QUIZ_ID_1)).thenReturn(
				createQuiz());
			when(userQuizSetRepository.getWithUserQuizByQuizSetId(EXPECTED_QUIZ_SET_ID, EXPECTED_SOCIAL_ACCOUNT_ID))
				.thenReturn(userQuizSet);

			// when
			SubmitUserQuizResponse response = sut.submitUserQuizAnswer(EXPECTED_QUIZ_SET_ID, EXPECTED_QUIZ_ID_1, request,
				EXPECTED_SOCIAL_ACCOUNT_ID);

			// then
			assertThat(response).isNotNull();
			assertThat(response.isCorrect()).isTrue();
			assertThat(response.explanation()).isEqualTo("테스트 설명1");

			verify(quizQueryRepository, times(1)).getWithQuizOptionById(EXPECTED_QUIZ_SET_ID, EXPECTED_QUIZ_ID_1);
			verify(userQuizSetRepository, times(1)).getWithUserQuizByQuizSetId(EXPECTED_QUIZ_SET_ID,
				EXPECTED_SOCIAL_ACCOUNT_ID);
		}

		@Test
		@DisplayName("퀴즈 답안 제출 성공 - 오답인 경우")
		void submit_user_quiz_answer_success_incorrect() {
			// given
			Set<Long> incorrectAnswerIds = Set.of(2L);
			SubmitUserQuizRequest request = new SubmitUserQuizRequest(incorrectAnswerIds);
			UserQuizSet userQuizSet = createUserQuizSetWithUserQuiz();

			// stubbing
			when(quizQueryRepository.getWithQuizOptionById(EXPECTED_QUIZ_SET_ID, EXPECTED_QUIZ_ID_1)).thenReturn(
				createQuiz());
			when(userQuizSetRepository.getWithUserQuizByQuizSetId(EXPECTED_QUIZ_SET_ID, EXPECTED_SOCIAL_ACCOUNT_ID))
				.thenReturn(userQuizSet);

			// when
			SubmitUserQuizResponse response = sut.submitUserQuizAnswer(EXPECTED_QUIZ_SET_ID, EXPECTED_QUIZ_ID_1, request,
				EXPECTED_SOCIAL_ACCOUNT_ID);

			// then
			assertThat(response).isNotNull();
			assertThat(response.isCorrect()).isFalse();
			assertThat(response.explanation()).isEqualTo("테스트 설명1");

			verify(quizQueryRepository, times(1)).getWithQuizOptionById(EXPECTED_QUIZ_SET_ID, EXPECTED_QUIZ_ID_1);
			verify(userQuizSetRepository, times(1)).getWithUserQuizByQuizSetId(EXPECTED_QUIZ_SET_ID,
				EXPECTED_SOCIAL_ACCOUNT_ID);
		}

		private UserQuizSet createUserQuizSetWithUserQuiz() {
			UserQuizSet userQuizSet = UserQuizSet.create(EXPECTED_QUIZ_SET_ID);
			ReflectionTestUtils.setField(userQuizSet, "id", 1L);
			ReflectionTestUtils.setField(userQuizSet, "createdBy", EXPECTED_SOCIAL_ACCOUNT_ID);

			UserQuiz userQuiz = UserQuiz.create(EXPECTED_QUIZ_ID_1, userQuizSet);
			userQuizSet.addUserQuizList(List.of(userQuiz));

			return userQuizSet;
		}
	}

	@Nested
	class EvaluateUserQuizSet {

		@Test
		@DisplayName("유저 퀴즈 세트 평가 성공")
		void evaluate_user_quiz_set_success() {
			// given
			final int likeScore = 5;
			final EvaluateQuizSetRequest request = new EvaluateQuizSetRequest(EXPECTED_USER_QUIZ_SET_ID, likeScore);
			final UserQuizSet userQuizSet = createUserQuizSet();
			userQuizSet.complete();

			// stubbing
			when(userQuizSetRepository.getWithUserQuizByIdAndQuizSetId(EXPECTED_USER_QUIZ_SET_ID, EXPECTED_QUIZ_SET_ID,
				EXPECTED_SOCIAL_ACCOUNT_ID)).thenReturn(userQuizSet);

			// when
			final EvaluateQuizSetResponse response = sut.evaluateUserQuizSet(EXPECTED_QUIZ_SET_ID, request,
				EXPECTED_SOCIAL_ACCOUNT_ID);

			// then
			assertThat(response).isNotNull();
			verify(userQuizSetRepository, times(1)).getWithUserQuizByIdAndQuizSetId(EXPECTED_USER_QUIZ_SET_ID,
				EXPECTED_QUIZ_SET_ID, EXPECTED_SOCIAL_ACCOUNT_ID);
		}

		@Test
		@DisplayName("유저 퀴즈 세트 평가 실패 - 완료되지 않은 퀴즈셋 평가 시도")
		void evaluate_incomplete_user_quiz_set_fail() {
			// given
			final int likeScore = 5;
			final EvaluateQuizSetRequest request = new EvaluateQuizSetRequest(EXPECTED_USER_QUIZ_SET_ID, likeScore);
			final UserQuizSet userQuizSet = createUserQuizSet();

			// stubbing
			when(userQuizSetRepository.getWithUserQuizByIdAndQuizSetId(EXPECTED_USER_QUIZ_SET_ID, EXPECTED_QUIZ_SET_ID,
				EXPECTED_SOCIAL_ACCOUNT_ID)).thenReturn(userQuizSet);

			// when && then
			assertThatThrownBy(() -> sut.evaluateUserQuizSet(EXPECTED_QUIZ_SET_ID, request, EXPECTED_SOCIAL_ACCOUNT_ID))
				.isInstanceOf(ServiceException.class)
				.hasMessage(NOT_COMPLETE_USER_QUIZ_SET.getMessage());
		}

		@Test
		@DisplayName("유저 퀴즈 세트 평가 실패 - 이미 평가한 퀴즈셋 평가 시도")
		void evaluate_already_evaluated_user_quiz_set_fail() {
			// given
			final int likeScore = 5;
			final EvaluateQuizSetRequest request = new EvaluateQuizSetRequest(EXPECTED_USER_QUIZ_SET_ID, likeScore);
			final UserQuizSet userQuizSet = createUserQuizSet();
			userQuizSet.complete();
			userQuizSet.evaluate(likeScore);

			// stubbing
			when(userQuizSetRepository.getWithUserQuizByIdAndQuizSetId(EXPECTED_USER_QUIZ_SET_ID, EXPECTED_QUIZ_SET_ID,
				EXPECTED_SOCIAL_ACCOUNT_ID)).thenReturn(userQuizSet);

			// when && then
			assertThatThrownBy(() -> sut.evaluateUserQuizSet(EXPECTED_QUIZ_SET_ID, request, EXPECTED_SOCIAL_ACCOUNT_ID))
				.isInstanceOf(ServiceException.class)
				.hasMessage(ALREADY_EVALUATED_USER_QUIZ_SET.getMessage());
		}
	}

	private QuizSet createQuizSet() {
		QuizSet quizSet = QuizSet.create(1L, 1L);
		ReflectionTestUtils.setField(quizSet, "id", EXPECTED_QUIZ_SET_ID);

		Quiz quiz1 = Quiz.create("질문1", "설명1", quizSet);
		ReflectionTestUtils.setField(quiz1, "id", EXPECTED_QUIZ_ID_1);

		QuizOption option1 = QuizOption.create("보기1", true, quiz1);
		QuizOption option2 = QuizOption.create("보기2", false, quiz1);
		ReflectionTestUtils.setField(option1, "id", 1L);
		ReflectionTestUtils.setField(option2, "id", 2L);

		quiz1.addQuizOptionList(List.of(option1, option2));

		Quiz quiz2 = Quiz.create("질문2", "설명2", quizSet);
		ReflectionTestUtils.setField(quiz2, "id", EXPECTED_QUIZ_ID_2);

		QuizOption option3 = QuizOption.create("A", true, quiz2);
		QuizOption option4 = QuizOption.create("B", false, quiz2);
		ReflectionTestUtils.setField(option3, "id", 3L);
		ReflectionTestUtils.setField(option4, "id", 4L);

		quiz2.addQuizOptionList(List.of(option3, option4));

		quizSet.addQuizList(List.of(quiz1, quiz2));

		return quizSet;
	}

	private Quiz createQuiz() {
		QuizSet quizSet = QuizSet.create(1L, 1L);
		Quiz quiz = Quiz.create("테스트 질문1", "테스트 설명1", quizSet);
		ReflectionTestUtils.setField(quiz, "id", EXPECTED_QUIZ_ID_1);

		QuizOption correctOption = QuizOption.create("정답", true, quiz);
		QuizOption incorrectOption = QuizOption.create("오답", false, quiz);
		ReflectionTestUtils.setField(correctOption, "id", 1L);
		ReflectionTestUtils.setField(incorrectOption, "id", 2L);

		quiz.addQuizOptionList(List.of(correctOption, incorrectOption));

		return quiz;
	}

	private UserQuizSet createUserQuizSet() {
		UserQuizSet userQuizSet = UserQuizSet.create(EXPECTED_QUIZ_SET_ID);
		ReflectionTestUtils.setField(userQuizSet, "id", EXPECTED_USER_QUIZ_SET_ID);
		ReflectionTestUtils.setField(userQuizSet, "createdBy", EXPECTED_SOCIAL_ACCOUNT_ID);

		UserQuiz userQuiz1 = UserQuiz.create(EXPECTED_QUIZ_ID_1, userQuizSet);
		ReflectionTestUtils.setField(userQuiz1, "firstAttemptCorrect", true);
		ReflectionTestUtils.setField(userQuiz1, "currentAttemptCorrect", true);

		UserQuiz userQuiz2 = UserQuiz.create(EXPECTED_QUIZ_ID_2, userQuizSet);
		ReflectionTestUtils.setField(userQuiz2, "firstAttemptCorrect", false);
		ReflectionTestUtils.setField(userQuiz2, "currentAttemptCorrect", true);

		userQuizSet.addUserQuizList(List.of(userQuiz1, userQuiz2));
		return userQuizSet;
	}
}