package com.readup.server.quiz.application;

import static com.readup.server.common.exception.ErrorCode.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import com.readup.server.book.domain.Book;
import com.readup.server.book.domain.Chapter;
import com.readup.server.book.domain.repository.BookRepository;
import com.readup.server.common.exception.RepositoryException;
import com.readup.server.common.exception.ServiceException;
import com.readup.server.quiz.application.dto.CreateQuizSetRequest;
import com.readup.server.quiz.application.dto.CreateQuizSetRequest.CreateQuizRequest;
import com.readup.server.quiz.application.dto.CreateQuizSetRequest.CreateQuizRequest.CreateQuizOptionRequest;
import com.readup.server.quiz.application.dto.CreateQuizSetResponse;
import com.readup.server.quiz.application.dto.CreateQuizSetResponse.CreateQuizResponse;
import com.readup.server.quiz.application.dto.GetQuizSetResponse;
import com.readup.server.quiz.application.dto.GetQuizSetResponse.GetQuizOptionResponse;
import com.readup.server.quiz.application.dto.GetQuizSetResponse.GetQuizResponse;
import com.readup.server.quiz.domain.model.Quiz;
import com.readup.server.quiz.domain.model.QuizOption;
import com.readup.server.quiz.domain.model.QuizSet;
import com.readup.server.quiz.stub.QuizSetJpaRepositoryStub;

@ExtendWith(MockitoExtension.class)
class QuizSetServiceTest {

	@InjectMocks
	private QuizSetService sut;

	@Spy
	private QuizSetJpaRepositoryStub quizSetJpaRepositoryStub;

	@Mock
	private BookRepository bookRepository;

	private static final Long EXPECTED_BOOK_ID = 1L;
	private static final Long EXPECTED_CHAPTER_ID = 1L;

	@Nested
	class CreateQuizSet {

		Book book;

		@BeforeEach
		void setUp() {
			Chapter chapter = createChapter();
			book = createBook(chapter);
		}

		@ParameterizedTest
		@MethodSource("provideSingleAndMultipleQuizRequests")
		@DisplayName("퀴즈 세트 등록 성공 - 단일 퀴즈 또는 다중 퀴즈")
		void create_quiz_set_success_parametrized(List<CreateQuizRequest> quizRequests) {
			// given
			CreateQuizSetRequest request = new CreateQuizSetRequest(EXPECTED_BOOK_ID, EXPECTED_CHAPTER_ID,
				quizRequests);
			when(bookRepository.getBookById(EXPECTED_BOOK_ID)).thenReturn(book);

			// when
			CreateQuizSetResponse response = sut.createQuizSet(request);

			// then
			assertBasicResponse(response, quizRequests.size());

			for (int i = 0; i < quizRequests.size(); i++) {
				CreateQuizRequest expected = quizRequests.get(i);
				CreateQuizResponse actual = response.quizResponseList().get(i);
				assertQuizEquals(actual, expected.question(), expected.explanation(),
					expected.quizOptionRequestList().size());
			}

			verify(quizSetJpaRepositoryStub, times(1)).save(any());
		}

		static Stream<List<CreateQuizRequest>> provideSingleAndMultipleQuizRequests() {
			CreateQuizOptionRequest option1 = new CreateQuizOptionRequest("보기1", false);
			CreateQuizOptionRequest option2 = new CreateQuizOptionRequest("보기2", true);
			CreateQuizOptionRequest option3 = new CreateQuizOptionRequest("A", true);
			CreateQuizOptionRequest option4 = new CreateQuizOptionRequest("B", false);

			return Stream.of(
				List.of(
					new CreateQuizRequest("질문1", "설명1", List.of(option1, option2))),

				List.of(
					new CreateQuizRequest("질문1", "설명1", List.of(option1, option2)),
					new CreateQuizRequest("질문2", "설명2", List.of(option3, option4))));
		}

		@Test
		@DisplayName("책이 존재하지 않는 경우 실패")
		void create_quiz_set_not_found_book() {
			// given
			Long nonExistingBookId = 999L;

			CreateQuizSetRequest mockRequest = mock(CreateQuizSetRequest.class);
			when(mockRequest.bookId()).thenReturn(nonExistingBookId);

			// stubbing
			when(bookRepository.getBookById(nonExistingBookId))
				.thenThrow(new RepositoryException(BOOK_NOT_FOUND));

			// when & then
			RepositoryException exception = assertThrows(RepositoryException.class,
				() -> sut.createQuizSet(mockRequest));
			assertEquals(BOOK_NOT_FOUND, exception.getErrorCode());
			verify(quizSetJpaRepositoryStub, never()).save(any());
		}

		@Test
		@DisplayName("챕터가 존재하지 않는 경우 실패")
		void create_quiz_set_not_found_chapter() {
			// given
			Long nonExistingChapterId = 999L;

			CreateQuizSetRequest mockRequest = mock(CreateQuizSetRequest.class);

			// stubbing
			when(mockRequest.bookId()).thenReturn(EXPECTED_BOOK_ID);
			when(mockRequest.chapterId()).thenReturn(nonExistingChapterId);
			when(bookRepository.getBookById(EXPECTED_BOOK_ID)).thenReturn(book);

			// when & then
			ServiceException exception = assertThrows(ServiceException.class, () -> sut.createQuizSet(mockRequest));
			assertEquals(NOT_FOUND_CHAPTER, exception.getErrorCode());
			verify(quizSetJpaRepositoryStub, never()).save(any());
		}

		private void assertBasicResponse(CreateQuizSetResponse response, int expectedQuizSize) {
			assertNotNull(response);
			assertEquals(EXPECTED_BOOK_ID, response.bookId());
			assertEquals(EXPECTED_CHAPTER_ID, response.chapterId());
			assertEquals(expectedQuizSize, response.quizResponseList().size());
		}

		private void assertQuizEquals(CreateQuizResponse actual, String expectedQuestion, String expectedExplanation,
			int expectedOptionSize) {
			assertEquals(expectedQuestion, actual.question());
			assertEquals(expectedExplanation, actual.explanation());
			assertEquals(expectedOptionSize, actual.quizOptionResponseList().size());
		}

		private Book createBook(Chapter chapter) {
			return Book.builder()
				.id(EXPECTED_BOOK_ID)
				.chapterList(List.of(chapter))
				.build();
		}

		private Chapter createChapter() {
			return Chapter.builder()
				.id(EXPECTED_CHAPTER_ID)
				.build();
		}
	}

	@Nested
	class GetQuiz {

		@ParameterizedTest
		@MethodSource("provideSingleAndMultipleQuizSets")
		@DisplayName("퀴즈 세트 조회 성공 - 단일 퀴즈 또는 다중 퀴즈")
		void get_quiz_set_success(QuizSet quizSet) {
			// given
			final int quizSequence = 2;
			QuizSet savedQuizSet = quizSetJpaRepositoryStub.save(quizSet);
			final int responseQuizCount = Integer.max(savedQuizSet.getQuizList().size() - quizSequence + 1, 0);

			// when
			GetQuizSetResponse response = sut.getQuizSet(savedQuizSet.getId(), quizSequence);

			// then
			Assertions.assertAll(
				() -> assertThat(response).isNotNull(),
				() -> assertThat(response.bookId()).isEqualTo(EXPECTED_BOOK_ID),
				() -> assertThat(response.chapterId()).isEqualTo(EXPECTED_CHAPTER_ID),
				() -> assertThat(response.quizSetId()).isEqualTo(savedQuizSet.getId()),
				() -> assertThat(response.quizResponseList()).hasSize(responseQuizCount));

			for (int i = quizSequence; i < quizSet.getQuizList().size(); i++) {
				Quiz quiz = quizSet.getQuizList().get(i);
				GetQuizResponse quizResponse = response.quizResponseList().get(i);

				assertThat(quizResponse.question()).isEqualTo(quiz.getQuestion());
				assertThat(quizResponse.quizOptionResponseList()).hasSize(quiz.getQuizOptionList().size());

				for (int j = 0; j < quiz.getQuizOptionList().size(); j++) {
					QuizOption option = quiz.getQuizOptionList().get(j);
					GetQuizOptionResponse optionResponse = quizResponse.quizOptionResponseList().get(j);
					assertThat(optionResponse.content()).isEqualTo(option.getContent());
				}
			}
		}

		static Stream<QuizSet> provideSingleAndMultipleQuizSets() {
			QuizSet singleQuizSet = QuizSet.create(EXPECTED_BOOK_ID, EXPECTED_CHAPTER_ID,
				List.of(new CreateQuizRequest("질문1", "설명1",
					List.of(new CreateQuizOptionRequest("보기1", true), new CreateQuizOptionRequest("보기2", false)))));

			QuizSet multipleQuizSet = QuizSet.create(EXPECTED_BOOK_ID, EXPECTED_CHAPTER_ID,
				List.of(
					new CreateQuizRequest("질문1", "설명1", List.of(
						new CreateQuizOptionRequest("A", true),
						new CreateQuizOptionRequest("B", false))),
					new CreateQuizRequest("질문2", "설명2", List.of(
						new CreateQuizOptionRequest("C", false),
						new CreateQuizOptionRequest("D", true)))
				));

			return Stream.of(singleQuizSet, multipleQuizSet);
		}

		@Test
		@DisplayName("퀴즈 세트 조회 실패 - 퀴즈 세트가 존재하지 않는 경우")
		void get_quiz_set_fail_quiz_set_not_found() {
			// given
			final int quizSequence = 2;
			final Long nonExistingQuizSetId = 999L;

			// when & then
			RepositoryException exception = assertThrows(RepositoryException.class,
				() -> sut.getQuizSet(nonExistingQuizSetId, quizSequence));
			assertThat(exception.getErrorCode()).isEqualTo(NOT_FOUND_QUIZ_SET);
			verify(quizSetJpaRepositoryStub, times(1)).getQuizSetById(nonExistingQuizSetId);
		}
	}
}