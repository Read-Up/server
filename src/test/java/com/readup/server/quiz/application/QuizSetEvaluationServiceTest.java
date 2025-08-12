package com.readup.server.quiz.application;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import com.readup.server.quiz.domain.model.Quiz;
import com.readup.server.quiz.domain.model.QuizOption;
import com.readup.server.quiz.domain.model.QuizSet;
import com.readup.server.quiz.domain.repository.QuizSetRepository;
import com.readup.server.user_quiz.event.QuizSetEvaluatedEvent;

@ExtendWith(MockitoExtension.class)
class QuizSetEvaluationServiceTest {

	@InjectMocks
	private QuizSetEvaluationService sut;

	@Mock
	private QuizSetRepository quizSetRepository;

	private static final Long EXPECTED_QUIZ_SET_ID = 1L;
	private static final Long EXPECTED_USER_QUIZ_SET_ID = 1L;
	private static final Long EXPECTED_QUIZ_ID_1 = 1L;
	private static final Long EXPECTED_QUIZ_ID_2 = 2L;

	@Nested
	class ApplyUserEvaluation {

		@Test
		@DisplayName("유저 퀴즈 세트 평가 반영 성공")
		void apply_user_evaluation_success() {
			// given
			final QuizSet quizSet = createQuizSet();
			final Double correctAnswerAverage = 0.5;
			final Integer likeScore = 3;
			final QuizSetEvaluatedEvent event = new QuizSetEvaluatedEvent(EXPECTED_QUIZ_SET_ID,
				EXPECTED_USER_QUIZ_SET_ID, correctAnswerAverage, likeScore);

			// stubbing
			when(quizSetRepository.getQuizSetById(event.quizSetId()))
				.thenReturn(quizSet);

			// when
			sut.applyUserEvaluation(event);

			// then
			verify(quizSetRepository, times(1)).getQuizSetById(event.quizSetId());
			assertEquals(correctAnswerAverage, quizSet.getCorrectAnswerAverage());
			assertEquals((double)likeScore, quizSet.getLikeAverage());
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
}