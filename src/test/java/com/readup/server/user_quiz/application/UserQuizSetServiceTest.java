package com.readup.server.user_quiz.application;

import static com.readup.server.common.exception.ErrorCode.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
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

import com.readup.server.common.exception.RepositoryException;
import com.readup.server.quiz.application.dto.CreateQuizSetRequest.CreateQuizRequest;
import com.readup.server.quiz.application.dto.CreateQuizSetRequest.CreateQuizRequest.CreateQuizOptionRequest;
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
import com.readup.server.user_quiz.stub.UserQuizSetJpaRepositoryStub;

@ExtendWith(MockitoExtension.class)
class UserQuizSetServiceTest {

	@InjectMocks
	private UserQuizSetService sut;

	@Spy
	private UserQuizSetJpaRepositoryStub userQuizSetJpaRepositoryStub;

	@Mock
	private QuizSetRepository quizSetRepository;

	@Mock
	private QuizQueryRepository quizQueryRepository;

	private static final Long EXPECTED_QUIZ_SET_ID = 1L;
	private static final Long EXPECTED_QUIZ_ID = 10L;
	private static final Long EXPECTED_USER_ID = 100L;

	private AuthUser authUser;
	private QuizSet quizSet;

	@BeforeEach
	void setUp() {
		authUser = new AuthUser(EXPECTED_USER_ID, "testUser");
		quizSet = createQuizSet();
		reset(userQuizSetJpaRepositoryStub);
	}

	@Nested
	class GetUserQuizSet {

		private final Long nonExistingQuizSetId = 999L;
		private UserQuizSet existingUserQuizSet;

		@BeforeEach
		void setUp() {
			existingUserQuizSet = createExistingUserQuizSet(
				quizSet.getQuizList().stream().map(Quiz::getId).toList());
		}

		@Test
		@DisplayName("사용자 퀴즈 세트 조회 성공 - 사용자 기존 퀴즈 세트 존재")
		void get_user_quiz_set_success_existing_user_quiz_set() {
			// stubbing
			when(quizSetRepository.getQuizSetById(EXPECTED_QUIZ_SET_ID)).thenReturn(quizSet);

			// when
			GetUserQuizSetResponse response = sut.getUserQuizSet(EXPECTED_QUIZ_SET_ID, authUser);

			// then
			assertThat(response).isNotNull();
			assertThat(response.userQuizSetId()).isEqualTo(existingUserQuizSet.getId());
			assertThat(response.quizSequence()).isEqualTo(existingUserQuizSet.getQuizSequence());
			assertThat(response.isEvaluated()).isEqualTo(existingUserQuizSet.getIsEvaluated());

			verify(quizSetRepository, times(1)).getQuizSetById(EXPECTED_QUIZ_SET_ID);
		}

		@Test
		@DisplayName("사용자 퀴즈 세트 조회 성공 - 처음으로 퀴즈 세트 푸는 경우 사용자 퀴즈 세트 생성")
		void get_user_quiz_set_success_new_user_quiz_set() {
			// given
			int initQuizSequence = 1;
			userQuizSetJpaRepositoryStub.clear();

			// stubbing
			when(quizSetRepository.getQuizSetById(EXPECTED_QUIZ_SET_ID)).thenReturn(quizSet);

			// when
			GetUserQuizSetResponse response = sut.getUserQuizSet(EXPECTED_QUIZ_SET_ID, authUser);

			// then
			assertThat(response).isNotNull();
			assertThat(response.userQuizSetId()).isEqualTo(userQuizSetJpaRepositoryStub.getCurrentId());
			assertThat(response.quizSequence()).isEqualTo(initQuizSequence);
			assertThat(response.isEvaluated()).isFalse();

			verify(quizSetRepository, times(1)).getQuizSetById(EXPECTED_QUIZ_SET_ID);
		}

		@Test
		@DisplayName("퀴즈 세트 조회 실패 - 퀴즈 세트가 존재하지 않는 경우")
		void get_user_quiz_set_fail_quiz_set_not_found() {
			// stubbing
			when(quizSetRepository.getQuizSetById(nonExistingQuizSetId))
				.thenThrow(new RepositoryException(NOT_FOUND_QUIZ_SET));

			// when & then
			RepositoryException exception = assertThrows(RepositoryException.class,
				() -> sut.getUserQuizSet(nonExistingQuizSetId, authUser));

			assertThat(exception.getErrorCode()).isEqualTo(NOT_FOUND_QUIZ_SET);

			verify(quizSetRepository, times(1)).getQuizSetById(nonExistingQuizSetId);
		}
	}

	@Nested
	class SubmitUserQuizAnswer {

		private Quiz quiz;
		private UserQuiz userQuiz;
		private UserQuizSet userQuizSet;

		@BeforeEach
		void setUp() {
			quiz = createQuiz();
			userQuizSet = createUserQuizSetWithUserQuiz();
			userQuiz = userQuizSet.getUserQuizList().getFirst();
		}

		@Test
		@DisplayName("퀴즈 답안 제출 성공 - 정답인 경우")
		void submit_user_quiz_answer_success_correct_answer() {
			// given
			Set<Integer> correctAnswerSequences = Set.of(1);
			SubmitUserQuizRequest request = new SubmitUserQuizRequest(correctAnswerSequences);

			// stubbing
			when(quizQueryRepository.getQuizWithQuizOptionById(EXPECTED_QUIZ_SET_ID, EXPECTED_QUIZ_ID))
				.thenReturn(quiz);

			// when
			SubmitUserQuizResponse response = sut.submitUserQuizAnswer(
				EXPECTED_QUIZ_SET_ID, EXPECTED_QUIZ_ID, request, authUser);

			// then
			assertThat(response).isNotNull();
			assertThat(response.isCorrect()).isTrue();
			assertThat(response.explanation()).isEqualTo(quiz.getExplanation());
			assertThat(userQuiz.getIsCorrect()).isTrue();

			verify(quizQueryRepository, times(1))
				.getQuizWithQuizOptionById(EXPECTED_QUIZ_SET_ID, EXPECTED_QUIZ_ID);
		}

		@Test
		@DisplayName("퀴즈 답안 제출 성공 - 오답인 경우")
		void submit_user_quiz_answer_success_incorrect_answer() {
			// given
			Set<Integer> incorrectAnswerSequences = Set.of(2);
			SubmitUserQuizRequest request = new SubmitUserQuizRequest(incorrectAnswerSequences);

			// stubbing
			when(quizQueryRepository.getQuizWithQuizOptionById(EXPECTED_QUIZ_SET_ID, EXPECTED_QUIZ_ID))
				.thenReturn(quiz);

			// when
			SubmitUserQuizResponse response = sut.submitUserQuizAnswer(
				EXPECTED_QUIZ_SET_ID, EXPECTED_QUIZ_ID, request, authUser);

			// then
			assertThat(response).isNotNull();
			assertThat(response.isCorrect()).isFalse();
			assertThat(response.explanation()).isNull();
			assertThat(userQuiz.getIsCorrect()).isFalse();

			verify(quizQueryRepository, times(1))
				.getQuizWithQuizOptionById(EXPECTED_QUIZ_SET_ID, EXPECTED_QUIZ_ID);
		}

		@Test
		@DisplayName("퀴즈 답안 제출 실패 - 퀴즈가 존재하지 않는 경우")
		void submit_user_quiz_answer_fail_quiz_not_found() {
			// given
			Set<Integer> answerSequences = Set.of(1);
			SubmitUserQuizRequest request = new SubmitUserQuizRequest(answerSequences);

			// stubbing
			when(quizQueryRepository.getQuizWithQuizOptionById(EXPECTED_QUIZ_SET_ID, EXPECTED_QUIZ_ID))
				.thenThrow(new RepositoryException(NOT_FOUND_QUIZ));

			// when & then
			RepositoryException exception = assertThrows(RepositoryException.class,
				() -> sut.submitUserQuizAnswer(EXPECTED_QUIZ_SET_ID, EXPECTED_QUIZ_ID, request, authUser));

			assertThat(exception.getErrorCode()).isEqualTo(NOT_FOUND_QUIZ);

			verify(quizQueryRepository, times(1))
				.getQuizWithQuizOptionById(EXPECTED_QUIZ_SET_ID, EXPECTED_QUIZ_ID);
		}

		@Test
		@DisplayName("퀴즈 답안 제출 실패 - 사용자 퀴즈가 존재하지 않는 경우")
		void submit_user_quiz_answer_fail_user_quiz_not_found() {
			// given
			Set<Integer> answerSequences = Set.of(1);
			SubmitUserQuizRequest request = new SubmitUserQuizRequest(answerSequences);
			userQuizSetJpaRepositoryStub.clear();

			// stubbing
			when(quizQueryRepository.getQuizWithQuizOptionById(EXPECTED_QUIZ_SET_ID, EXPECTED_QUIZ_ID))
				.thenReturn(quiz);

			// when & then
			RepositoryException exception = assertThrows(RepositoryException.class,
				() -> sut.submitUserQuizAnswer(EXPECTED_QUIZ_SET_ID, EXPECTED_QUIZ_ID, request, authUser));

			assertThat(exception.getErrorCode()).isEqualTo(NOT_FOUND_USER_QUIZ_SET);

			verify(quizQueryRepository, times(1))
				.getQuizWithQuizOptionById(EXPECTED_QUIZ_SET_ID, EXPECTED_QUIZ_ID);
		}
	}

	private QuizSet createQuizSet() {
		List<CreateQuizRequest> quizRequests = List.of(
			new CreateQuizRequest(
				"질문1", "설명1", List.of(
				new CreateQuizOptionRequest("보기1", true),
				new CreateQuizOptionRequest("보기2", false))),
			new CreateQuizRequest(
				"질문2", "설명2", List.of(
				new CreateQuizOptionRequest("A", true),
				new CreateQuizOptionRequest("B", false))));
		return QuizSet.create(1L, 1L, quizRequests);
	}

	private Quiz createQuiz() {
		List<CreateQuizOptionRequest> optionRequests = List.of(
			new CreateQuizOptionRequest("정답 보기", true),
			new CreateQuizOptionRequest("오답 보기", false)
		);

		QuizSet mockQuizSet = mock(QuizSet.class);
		Quiz quiz = Quiz.create(1, "테스트 질문", "테스트 설명", mockQuizSet, optionRequests);
		ReflectionTestUtils.setField(quiz, "id", EXPECTED_QUIZ_ID);
		return quiz;
	}

	private UserQuizSet createExistingUserQuizSet(List<Long> quizIdList) {
		UserQuizSet userQuizSet = UserQuizSet.create(EXPECTED_QUIZ_SET_ID, quizIdList);
		ReflectionTestUtils.setField(userQuizSet, "createdBy", EXPECTED_USER_ID);
		return userQuizSetJpaRepositoryStub.save(userQuizSet);
	}

	private UserQuizSet createUserQuizSetWithUserQuiz() {
		List<Long> quizIdList = List.of(EXPECTED_QUIZ_ID);
		UserQuizSet userQuizSet = UserQuizSet.create(EXPECTED_QUIZ_SET_ID, quizIdList);
		ReflectionTestUtils.setField(userQuizSet, "createdBy", EXPECTED_USER_ID);
		return userQuizSetJpaRepositoryStub.save(userQuizSet);
	}
}