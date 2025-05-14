package com.readup.server.user_quiz.application;

import static com.readup.server.common.exception.ErrorCode.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

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

import com.readup.server.common.exception.ServiceException;
import com.readup.server.quiz.domain.model.Quiz;
import com.readup.server.quiz.domain.model.QuizSet;
import com.readup.server.quiz.domain.repository.QuizSetRepository;
import com.readup.server.user.dto.AuthUser;
import com.readup.server.user_quiz.application.dto.GetUserQuizSetResponse;
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

	private static final Long EXPECTED_QUIZ_SET_ID = 1L;
	private static final Long EXPECTED_USER_ID = 100L;

	@Nested
	class GetUserQuizSet {

		private UserQuizSet existingUserQuizSet;
		private QuizSet quizSet;
		private AuthUser authUser;

		@BeforeEach
		void setUp() {
			authUser = createAuthUser();
			quizSet = createQuizSet();
			existingUserQuizSet = createExistingUserQuizSet(quizSet.getQuizList());
			reset(userQuizSetJpaRepositoryStub);
		}

		@Test
		@DisplayName("사용자 퀴즈 세트 조회 성공 - 사용자 기존 퀴즈 세트 존재")
		void get_user_quiz_set_success_existing_user_quiz_set() {
			// stubbing
			when(quizSetRepository.findById(EXPECTED_QUIZ_SET_ID)).thenReturn(Optional.of(quizSet));
			when(userQuizSetJpaRepositoryStub.findByQuizSetIdAndCreatedBy(EXPECTED_QUIZ_SET_ID, EXPECTED_USER_ID))
				.thenReturn(Optional.of(existingUserQuizSet));

			// when
			GetUserQuizSetResponse response = sut.getUserQuizSet(EXPECTED_QUIZ_SET_ID, authUser);

			// then
			assertThat(response).isNotNull();
			assertThat(response.userQuizSetId()).isEqualTo(existingUserQuizSet.getId());
			assertThat(response.quizSequence()).isEqualTo(existingUserQuizSet.getQuizSequence());
			assertThat(response.isEvaluated()).isEqualTo(existingUserQuizSet.getIsEvaluated());

			verify(quizSetRepository, times(1)).findById(EXPECTED_QUIZ_SET_ID);
			verify(userQuizSetJpaRepositoryStub, times(1)).findByQuizSetIdAndCreatedBy(EXPECTED_QUIZ_SET_ID,
				EXPECTED_USER_ID);
			verify(userQuizSetJpaRepositoryStub, never()).save(any());
		}

		@Test
		@DisplayName("사용자 퀴즈 세트 조회 성공 - 처음으로 퀴즈 세트 푸는 경우 사용자 퀴즈 세트 생성")
		void get_user_quiz_set_success_new_user_quiz_set() {
			// given
			final int initQuizSequence = 1;

			// stubbing
			when(quizSetRepository.findById(EXPECTED_QUIZ_SET_ID)).thenReturn(Optional.of(quizSet));
			when(userQuizSetJpaRepositoryStub.findByQuizSetIdAndCreatedBy(EXPECTED_QUIZ_SET_ID, EXPECTED_USER_ID))
				.thenReturn(Optional.empty());

			// when
			GetUserQuizSetResponse response = sut.getUserQuizSet(EXPECTED_QUIZ_SET_ID, authUser);

			// then
			assertThat(response).isNotNull();
			assertThat(response.userQuizSetId()).isEqualTo(userQuizSetJpaRepositoryStub.size());
			assertThat(response.quizSequence()).isEqualTo(initQuizSequence);
			assertThat(response.isEvaluated()).isFalse();

			verify(quizSetRepository, times(1)).findById(EXPECTED_QUIZ_SET_ID);
			verify(userQuizSetJpaRepositoryStub, times(1))
				.findByQuizSetIdAndCreatedBy(EXPECTED_QUIZ_SET_ID, EXPECTED_USER_ID);
			verify(userQuizSetJpaRepositoryStub, times(1)).save(any());
		}

		@Test
		@DisplayName("퀴즈 세트 조회 실패 - 퀴즈 세트가 존재하지 않는 경우")
		void get_user_quiz_set_fail_quiz_set_not_found() {
			// given
			final Long nonExistingQuizSetId = 999L;

			// stubbing
			when(quizSetRepository.findById(nonExistingQuizSetId)).thenReturn(Optional.empty());

			// when & then
			ServiceException exception = assertThrows(ServiceException.class,
				() -> sut.getUserQuizSet(nonExistingQuizSetId, authUser));

			assertThat(exception.getErrorCode()).isEqualTo(NOT_FOUND_QUIZ_SET);

			verify(quizSetRepository, times(1)).findById(nonExistingQuizSetId);
			verify(userQuizSetJpaRepositoryStub, never()).findByQuizSetIdAndCreatedBy(anyLong(), anyLong());
			verify(userQuizSetJpaRepositoryStub, never()).save(any());
		}

		private AuthUser createAuthUser() {
			return new AuthUser(EXPECTED_USER_ID, "testUser");
		}

		private QuizSet createQuizSet() {
			QuizSet newQuizSet = QuizSet.create(1L, 1L);

			Quiz quiz1 = newQuizSet.addQuiz("질문1", "설명1");
			quiz1.addQuizOption("보기1", true);
			quiz1.addQuizOption("보기2", false);

			Quiz quiz2 = newQuizSet.addQuiz("질문2", "설명2");
			quiz2.addQuizOption("A", true);
			quiz2.addQuizOption("B", false);

			return newQuizSet;
		}

		private UserQuizSet createExistingUserQuizSet(List<Quiz> quizList) {
			UserQuizSet userQuizSet = UserQuizSet.create(EXPECTED_QUIZ_SET_ID);
			quizList.forEach(quiz -> userQuizSet.addUserQuiz(quiz.getId()));
			ReflectionTestUtils.setField(userQuizSet, "createdBy", EXPECTED_USER_ID);
			return userQuizSetJpaRepositoryStub.save(userQuizSet);
		}
	}
}