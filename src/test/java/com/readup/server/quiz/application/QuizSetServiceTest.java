package com.readup.server.quiz.application;

import static com.readup.server.common.exception.ErrorCode.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.stream.Stream;

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
import com.readup.server.quiz.stub.QuizSetJpaRepositoryStub;

@ExtendWith(MockitoExtension.class)
class QuizSetServiceTest {

	@InjectMocks
	private QuizSetService sut;

	@Spy
	private QuizSetJpaRepositoryStub quizSetJpaRepositoryStub;

	@Mock
	private BookRepository bookRepository;

	final Long bookId = 1L;
	final Long chapterId = 1L;

	Book book;

	@BeforeEach
	void setUp() {
		Chapter chapter = createChapter();
		book = createBook(chapter);
	}

	@Nested
	class CreateQuiz {

		@ParameterizedTest
		@MethodSource("provideSingleAndMultipleQuizRequests")
		@DisplayName("퀴즈 세트 등록 성공 - 단일 퀴즈 또는 다중 퀴즈")
		void create_quiz_success_parametrized(List<CreateQuizRequest> quizRequests) {
			// given
			CreateQuizSetRequest request = new CreateQuizSetRequest(bookId, chapterId, quizRequests);
			when(bookRepository.getBookById(bookId)).thenReturn(book);

			// when
			CreateQuizSetResponse response = sut.createQuizSet(request);

			// then
			assertBasicResponse(response, bookId, chapterId, quizRequests.size());

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
		void create_quiz_not_found_book() {
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
		void create_quiz_not_found_chapter() {
			// given
			Long nonExistingChapterId = 999L;

			CreateQuizSetRequest mockRequest = mock(CreateQuizSetRequest.class);

			// stubbing
			when(mockRequest.bookId()).thenReturn(bookId);
			when(mockRequest.chapterId()).thenReturn(nonExistingChapterId);
			when(bookRepository.getBookById(bookId)).thenReturn(book);

			// when & then
			ServiceException exception = assertThrows(ServiceException.class, () -> sut.createQuizSet(mockRequest));
			assertEquals(NOT_FOUND_CHAPTER, exception.getErrorCode());
			verify(quizSetJpaRepositoryStub, never()).save(any());
		}

		private void assertBasicResponse(CreateQuizSetResponse response, Long expectedBookId, Long expectedChapterId,
			int expectedQuizSize) {
			assertNotNull(response);
			assertEquals(expectedBookId, response.bookId());
			assertEquals(expectedChapterId, response.chapterId());
			assertEquals(expectedQuizSize, response.quizResponseList().size());
		}

		private void assertQuizEquals(CreateQuizResponse actual, String expectedQuestion, String expectedExplanation,
			int expectedOptionSize) {
			assertEquals(expectedQuestion, actual.question());
			assertEquals(expectedExplanation, actual.explanation());
			assertEquals(expectedOptionSize, actual.quizOptionResponseList().size());
		}
	}

	private Book createBook(Chapter chapter) {
		return Book.builder()
			.id(bookId)
			.chapterList(List.of(chapter))
			.build();
	}

	private Chapter createChapter() {
		return Chapter.builder()
			.id(chapterId)
			.build();
	}
}