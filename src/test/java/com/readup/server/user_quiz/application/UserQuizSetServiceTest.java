package com.readup.server.user_quiz.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import com.readup.server.quiz.domain.model.Quiz;
import com.readup.server.quiz.domain.model.QuizOption;
import com.readup.server.quiz.domain.model.QuizSet;
import com.readup.server.quiz.domain.repository.QuizQueryRepository;
import com.readup.server.quiz.domain.repository.QuizSetRepository;
import com.readup.server.user.dto.AuthUser;
import com.readup.server.user_quiz.application.dto.GetUserQuizSetResponse;
import com.readup.server.user_quiz.application.dto.SubmitUserQuizRequest;
import com.readup.server.user_quiz.application.dto.SubmitUserQuizResponse;
import com.readup.server.user_quiz.domain.model.UserQuiz;
import com.readup.server.user_quiz.domain.model.UserQuizSet;
import com.readup.server.user_quiz.stub.UserQuizSetJpaRepositoryStub;

@ExtendWith(MockitoExtension.class)
class UserQuizSetServiceTest {

	@InjectMocks
	private UserQuizSetService sut;

	@Mock
	private QuizSetRepository quizSetRepository;

	@Mock
	private QuizQueryRepository quizQueryRepository;

	@Spy
	private UserQuizSetJpaRepositoryStub userQuizSetRepository;

	private static final Long EXPECTED_QUIZ_SET_ID = 1L;
	private static final Long EXPECTED_USER_ID = 100L;
	private static final Long QUIZ_ID_1 = 1L;
	private static final Long QUIZ_ID_2 = 2L;

	private QuizSet commonQuizSet;
	private Quiz commonQuiz;
	private AuthUser commonAuthUser;

	@BeforeEach
	void setUpCommonEntities() {
		commonAuthUser = new AuthUser(EXPECTED_USER_ID, "testUser");
		commonQuizSet = createQuizSet();
		commonQuiz = createQuiz(QUIZ_ID_1, "테스트 질문1", "테스트 설명1");

		userQuizSetRepository.clear();
	}

	@Nested
	class GetUserQuizSet {

		@Test
		@DisplayName("사용자 퀴즈 세트 조회 성공 - 사용자 기존 퀴즈 세트 존재")
		void get_user_quiz_set_success_existing_user_quiz_set() {
			// given
			UserQuizSet existingUserQuizSet = createExistingUserQuizSet();
			userQuizSetRepository.save(existingUserQuizSet);

			// stubbing
			when(quizSetRepository.getQuizSetById(EXPECTED_QUIZ_SET_ID)).thenReturn(commonQuizSet);

			// when
			GetUserQuizSetResponse response = sut.getUserQuizSet(EXPECTED_QUIZ_SET_ID, commonAuthUser);

			// then
			assertThat(response).isNotNull();
			assertThat(response.userQuizSetId()).isEqualTo(existingUserQuizSet.getId());
			assertThat(response.lastQuizId()).isEqualTo(existingUserQuizSet.getLastQuizId());
			assertThat(response.isEvaluated()).isEqualTo(existingUserQuizSet.getIsEvaluated());

			verify(quizSetRepository, times(1)).getQuizSetById(EXPECTED_QUIZ_SET_ID);
		}

		@Test
		@DisplayName("사용자 퀴즈 세트 조회 성공 - 처음으로 퀴즈 세트 푸는 경우 사용자 퀴즈 세트 생성")
		void get_user_quiz_set_success_new_user_quiz_set() {
			// stubbing
			when(quizSetRepository.getQuizSetById(EXPECTED_QUIZ_SET_ID)).thenReturn(commonQuizSet);

			// when
			GetUserQuizSetResponse response = sut.getUserQuizSet(EXPECTED_QUIZ_SET_ID, commonAuthUser);

			// then
			assertThat(response).isNotNull();
			assertThat(response.userQuizSetId()).isEqualTo(userQuizSetRepository.getCurrentId());
			assertThat(response.lastQuizId()).isNull();
			assertThat(response.isEvaluated()).isFalse();

			verify(quizSetRepository, times(1)).getQuizSetById(EXPECTED_QUIZ_SET_ID);
		}

		private UserQuizSet createExistingUserQuizSet() {
			UserQuizSet userQuizSet = UserQuizSet.create(EXPECTED_QUIZ_SET_ID);
			ReflectionTestUtils.setField(userQuizSet, "createdBy", EXPECTED_USER_ID);

			List<UserQuiz> userQuizList = List.of(
				UserQuiz.create(QUIZ_ID_1, userQuizSet),
				UserQuiz.create(QUIZ_ID_2, userQuizSet)
			);

			userQuizSet.addUserQuizList(userQuizList);

			return userQuizSet;
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
			UserQuizSet userQuizSet = createUserQuizSetWithUserQuiz(QUIZ_ID_1);
			userQuizSetRepository.save(userQuizSet);

			// stubbing
			when(quizQueryRepository.getQuizWithQuizOptionById(EXPECTED_QUIZ_SET_ID, QUIZ_ID_1)).thenReturn(
				commonQuiz);

			// when
			SubmitUserQuizResponse response = sut.submitUserQuizAnswer(EXPECTED_QUIZ_SET_ID, QUIZ_ID_1, request,
				commonAuthUser);

			// then
			assertThat(response).isNotNull();
			assertThat(response.isCorrect()).isTrue();
			assertThat(response.explanation()).isEqualTo("테스트 설명1");

			verify(quizQueryRepository, times(1)).getQuizWithQuizOptionById(EXPECTED_QUIZ_SET_ID, QUIZ_ID_1);
		}

		@Test
		@DisplayName("퀴즈 답안 제출 성공 - 오답인 경우")
		void submit_user_quiz_answer_success_incorrect() {
			// given
			Set<Long> incorrectAnswerIds = Set.of(2L);
			SubmitUserQuizRequest request = new SubmitUserQuizRequest(incorrectAnswerIds);
			UserQuizSet userQuizSet = createUserQuizSetWithUserQuiz(QUIZ_ID_1);
			userQuizSetRepository.save(userQuizSet);

			// stubbing
			when(quizQueryRepository.getQuizWithQuizOptionById(EXPECTED_QUIZ_SET_ID, QUIZ_ID_1)).thenReturn(
				commonQuiz);

			// when
			SubmitUserQuizResponse response = sut.submitUserQuizAnswer(EXPECTED_QUIZ_SET_ID, QUIZ_ID_1, request,
				commonAuthUser);

			// then
			assertThat(response).isNotNull();
			assertThat(response.isCorrect()).isFalse();
			assertThat(response.explanation()).isNull();

			verify(quizQueryRepository, times(1)).getQuizWithQuizOptionById(EXPECTED_QUIZ_SET_ID, QUIZ_ID_1);
		}

		private UserQuizSet createUserQuizSetWithUserQuiz(Long quizId) {
			UserQuizSet userQuizSet = UserQuizSet.create(EXPECTED_QUIZ_SET_ID);
			ReflectionTestUtils.setField(userQuizSet, "createdBy", EXPECTED_USER_ID);

			UserQuiz userQuiz = UserQuiz.create(quizId, userQuizSet);
			userQuizSet.addUserQuizList(List.of(userQuiz));

			return userQuizSet;
		}
	}

	private QuizSet createQuizSet() {
		QuizSet quizSet = QuizSet.create(1L, 1L);
		ReflectionTestUtils.setField(quizSet, "id", EXPECTED_QUIZ_SET_ID);

		Quiz quiz1 = Quiz.create("질문1", "설명1", quizSet);
		ReflectionTestUtils.setField(quiz1, "id", QUIZ_ID_1);

		QuizOption option1 = QuizOption.create("보기1", true, quiz1);
		QuizOption option2 = QuizOption.create("보기2", false, quiz1);
		ReflectionTestUtils.setField(option1, "id", 1L);
		ReflectionTestUtils.setField(option2, "id", 2L);

		quiz1.addQuizOptionList(List.of(option1, option2));

		Quiz quiz2 = Quiz.create("질문2", "설명2", quizSet);
		ReflectionTestUtils.setField(quiz2, "id", QUIZ_ID_2);

		QuizOption option3 = QuizOption.create("A", true, quiz2);
		QuizOption option4 = QuizOption.create("B", false, quiz2);
		ReflectionTestUtils.setField(option3, "id", 3L);
		ReflectionTestUtils.setField(option4, "id", 4L);

		quiz2.addQuizOptionList(List.of(option3, option4));

		quizSet.addQuizList(List.of(quiz1, quiz2));

		return quizSet;
	}

	private Quiz createQuiz(Long quizId, String question, String explanation) {
		QuizSet quizSet = QuizSet.create(1L, 1L);
		Quiz quiz = Quiz.create(question, explanation, quizSet);
		ReflectionTestUtils.setField(quiz, "id", quizId);

		QuizOption correctOption = QuizOption.create("정답", true, quiz);
		QuizOption incorrectOption = QuizOption.create("오답", false, quiz);
		ReflectionTestUtils.setField(correctOption, "id", 1L);
		ReflectionTestUtils.setField(incorrectOption, "id", 2L);

		quiz.addQuizOptionList(List.of(correctOption, incorrectOption));

		return quiz;
	}
}