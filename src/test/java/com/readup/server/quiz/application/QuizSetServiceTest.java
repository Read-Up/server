package com.readup.server.quiz.application;

import static com.readup.server.common.exception.ErrorCode.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.util.ReflectionTestUtils.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.SliceImpl;

import com.readup.server.book.domain.Book;
import com.readup.server.book.domain.Chapter;
import com.readup.server.book.domain.repository.BookRepository;
import com.readup.server.common.exception.RepositoryException;
import com.readup.server.common.exception.ServiceException;
import com.readup.server.quiz.application.dto.CreateQuizSetRequest;
import com.readup.server.quiz.application.dto.CreateQuizSetRequest.CreateQuizRequest;
import com.readup.server.quiz.application.dto.CreateQuizSetRequest.CreateQuizRequest.CreateQuizOptionRequest;
import com.readup.server.quiz.application.dto.CreateQuizSetResponse;
import com.readup.server.quiz.application.dto.GetQuizSetPageResponse;
import com.readup.server.quiz.application.dto.GetQuizSetResponse;
import com.readup.server.quiz.application.dto.SliceResponse;
import com.readup.server.quiz.domain.model.Quiz;
import com.readup.server.quiz.domain.model.QuizOption;
import com.readup.server.quiz.domain.model.QuizSet;
import com.readup.server.quiz.domain.repository.QuizSetRepository;

@ExtendWith(MockitoExtension.class)
class QuizSetServiceTest {

	@InjectMocks
	private QuizSetService sut;

	@Spy
	private QuizSetRepository quizSetRepository;

	@Mock
	private BookRepository bookRepository;

	private static final Long BOOK_ID = 1L;
	private static final Long CHAPTER_ID = 1L;
	private static final Long QUIZ_SET_ID = 1L;
	private static final Long SOCIAL_ACCOUNT_ID = 1L;

	private Book book;

	@BeforeEach
	void setUp() {
		Chapter chapter = createChapter();
		book = createBook(List.of(chapter));
	}

	@Nested
	@DisplayName("퀴즈 세트 생성 테스트")
	class CreateQuizSetTest {

		@ParameterizedTest
		@ValueSource(ints = {1, 5})
		@DisplayName("퀴즈 세트 생성 성공 (단일, 다중 퀴즈)")
		void create_quizSet_success(int quizCount) {
			// given
			final CreateQuizSetRequest request = createQuizSetRequest(quizCount);
			final QuizSet savedQuizSet = createQuizSetWithId(request.toEntity());

			// stubbing
			when(bookRepository.getBookById(BOOK_ID)).thenReturn(book);
			when(quizSetRepository.save(any(QuizSet.class))).thenReturn(savedQuizSet);

			// when
			CreateQuizSetResponse response = sut.createQuizSet(request);

			// then
			assertQuizSetResponse(response, quizCount);
			verify(quizSetRepository, times(1)).save(any(QuizSet.class));
		}

		@Test
		@DisplayName("책이 존재하지 않는 경우 실패")
		void create_quizSet_fail_book_not_found() {
			// given
			final CreateQuizSetRequest request = createQuizSetRequest(1);

			// stubbing
			when(bookRepository.getBookById(BOOK_ID))
				.thenThrow(new RepositoryException(BOOK_NOT_FOUND));

			// when & then
			assertThatThrownBy(() -> sut.createQuizSet(request))
				.isInstanceOf(RepositoryException.class)
				.hasFieldOrPropertyWithValue("errorCode", BOOK_NOT_FOUND);

			verify(quizSetRepository, never()).save(any());
		}

		@Test
		@DisplayName("챕터가 존재하지 않는 경우 실패")
		void create_quizSet_fail_chapter_not_found() {
			// given
			final Long nonExistingChapterId = 999L;
			final CreateQuizSetRequest request = new CreateQuizSetRequest(BOOK_ID, nonExistingChapterId,
				List.of(createQuizRequest("질문", "설명")));

			// stubbing
			when(bookRepository.getBookById(BOOK_ID)).thenReturn(book);

			// when & then
			assertThatThrownBy(() -> sut.createQuizSet(request))
				.isInstanceOf(ServiceException.class)
				.hasFieldOrPropertyWithValue("errorCode", NOT_FOUND_CHAPTER);

			verify(quizSetRepository, never()).save(any());
		}
	}

	@Nested
	@DisplayName("퀴즈 세트 단건 조회 테스트")
	class GetQuizSetTest {

		@Test
		@DisplayName("퀴즈 세트 조회 성공")
		void get_quizSet_success() {
			// given
			final Long lastQuizId = 0L;

			// stubbing
			when(quizSetRepository.getQuizSetById(QUIZ_SET_ID)).thenReturn(createQuizSetWithId());

			// when
			GetQuizSetResponse response = sut.getQuizSet(QUIZ_SET_ID, lastQuizId);

			// then
			assertThat(response).isNotNull();
			assertThat(response.quizSetId()).isEqualTo(QUIZ_SET_ID);
			assertThat(response.bookId()).isEqualTo(BOOK_ID);
			assertThat(response.chapterId()).isEqualTo(CHAPTER_ID);
			verify(quizSetRepository, times(1)).getQuizSetById(QUIZ_SET_ID);
		}

		@Test
		@DisplayName("퀴즈 세트가 존재하지 않는 경우 실패")
		void get_quizSet_fail_quizSet_not_found() {
			// given
			final Long nonExistingQuizSetId = 999L;

			// stubbing
			when(quizSetRepository.getQuizSetById(nonExistingQuizSetId))
				.thenThrow(new RepositoryException(NOT_FOUND_QUIZ_SET));

			// when & then
			assertThatThrownBy(() -> sut.getQuizSet(nonExistingQuizSetId, 0L))
				.isInstanceOf(RepositoryException.class)
				.hasFieldOrPropertyWithValue("errorCode", NOT_FOUND_QUIZ_SET);

			verify(quizSetRepository, times(1)).getQuizSetById(nonExistingQuizSetId);
		}

		@ParameterizedTest
		@ValueSource(longs = {-1L, 0L, 1L, 2L})
		@DisplayName("lastQuizId에 따른 퀴즈 조회")
		void get_quizSet_success_with_last_quiz_id(Long lastQuizId) {
			// given
			final QuizSet quizSetWithMultipleQuizzes = createQuizSetWithMultipleQuizzes();

			// stubbing
			when(quizSetRepository.getQuizSetById(QUIZ_SET_ID)).thenReturn(quizSetWithMultipleQuizzes);

			// when
			GetQuizSetResponse response = sut.getQuizSet(QUIZ_SET_ID, lastQuizId);

			// then
			assertThat(response).isNotNull();
			assertThat(response.quizSetId()).isEqualTo(QUIZ_SET_ID);
			response.quizResponseList()
				.forEach(q -> assertThat(q.quizId()).isGreaterThan(lastQuizId));

			verify(quizSetRepository, times(1)).getQuizSetById(QUIZ_SET_ID);
		}
	}

	@Nested
	@DisplayName("전체 퀴즈 세트 페이지 조회 테스트")
	class GetAllQuizSetsTest {

		@Test
		@DisplayName("전체 퀴즈 세트 조회 성공")
		void get_all_quizSets_success() {
			// given
			final Pageable pageable = PageRequest.of(0, 10);
			final SliceResponse<GetQuizSetPageResponse> mockSliceResponse = createMockSliceResponse(5, pageable);

			// stubbing
			when(quizSetRepository.getAllQuizSets(BOOK_ID, CHAPTER_ID, pageable))
				.thenReturn(mockSliceResponse);

			// when
			SliceResponse<GetQuizSetPageResponse> result = sut.getAllQuizSets(BOOK_ID, CHAPTER_ID, pageable);

			// then
			assertThat(result.contentSize()).isEqualTo(5);
			assertThat(result.currentPage()).isEqualTo(pageable.getPageNumber());
			assertThat(result.pageSize()).isEqualTo(pageable.getPageSize());
			verify(quizSetRepository, times(1)).getAllQuizSets(BOOK_ID, CHAPTER_ID, pageable);
		}
	}

	@Nested
	@DisplayName("내 퀴즈 세트 페이지 조회 테스트")
	class GetMyQuizSetsTest {

		@Test
		@DisplayName("내 퀴즈 세트 조회 성공")
		void get_my_quizSets_success() {
			// given
			final Pageable pageable = PageRequest.of(0, 10);
			final SliceResponse<GetQuizSetPageResponse> mockSliceResponse = createMockSliceResponse(5, pageable);

			// stubbing
			when(quizSetRepository.getMyQuizSets(SOCIAL_ACCOUNT_ID, BOOK_ID, CHAPTER_ID, pageable))
				.thenReturn(mockSliceResponse);

			// when
			SliceResponse<GetQuizSetPageResponse> result = sut.getMyQuizSets(SOCIAL_ACCOUNT_ID, BOOK_ID, CHAPTER_ID,
				pageable);

			// then
			assertThat(result.contentSize()).isEqualTo(5);
			verify(quizSetRepository, times(1)).getMyQuizSets(SOCIAL_ACCOUNT_ID, BOOK_ID, CHAPTER_ID, pageable);
		}
	}

	@Nested
	@DisplayName("참여한 퀴즈 세트 페이지 조회 테스트")
	class GetParticipatingQuizSetsTest {

		@Test
		@DisplayName("참여한 퀴즈 세트 조회 성공")
		void getParticipatingQuizSets_Success() {
			// given
			final Pageable pageable = PageRequest.of(0, 10);
			final SliceResponse<GetQuizSetPageResponse> mockSliceResponse = createMockSliceResponse(3, pageable);

			// stubbing
			when(quizSetRepository.getParticipatingQuizSets(SOCIAL_ACCOUNT_ID, BOOK_ID, CHAPTER_ID, pageable))
				.thenReturn(mockSliceResponse);

			// when
			SliceResponse<GetQuizSetPageResponse> result = sut.getParticipatingQuizSets(SOCIAL_ACCOUNT_ID, BOOK_ID,
				CHAPTER_ID, pageable);

			// then
			assertThat(result.contentSize()).isEqualTo(3);
			verify(quizSetRepository, times(1)).getParticipatingQuizSets(SOCIAL_ACCOUNT_ID, BOOK_ID, CHAPTER_ID,
				pageable);
		}
	}

	private CreateQuizSetRequest createQuizSetRequest(int quizCount) {
		List<CreateQuizRequest> quizRequests = Stream.iterate(1, i -> i + 1)
			.limit(quizCount)
			.map(i -> createQuizRequest("질문" + i, "설명" + i))
			.toList();

		return new CreateQuizSetRequest(BOOK_ID, CHAPTER_ID, quizRequests);
	}

	private CreateQuizRequest createQuizRequest(String question, String explanation) {
		return new CreateQuizRequest(question, explanation, List.of(
			new CreateQuizOptionRequest("옵션1", true),
			new CreateQuizOptionRequest("옵션2", false)
		));
	}

	private Book createBook(List<Chapter> chapters) {
		return Book.builder()
			.id(BOOK_ID)
			.chapterList(chapters)
			.build();
	}

	private Chapter createChapter() {
		return Chapter.builder()
			.id(CHAPTER_ID)
			.build();
	}

	private QuizSet createQuizSetWithId() {
		QuizSet newQuizSet = QuizSet.create(BOOK_ID, CHAPTER_ID);
		setField(newQuizSet, "id", QUIZ_SET_ID);
		setField(newQuizSet, "createdAt", LocalDateTime.now());
		return newQuizSet;
	}

	private QuizSet createQuizSetWithId(QuizSet quizSet) {
		setField(quizSet, "id", QUIZ_SET_ID);
		setField(quizSet, "createdAt", LocalDateTime.now());

		long quizId = 1L;
		for (Quiz quiz : quizSet.getQuizList()) {
			setField(quiz, "id", quizId++);

			long optionId = 1L;
			for (QuizOption option : quiz.getQuizOptionList()) {
				setField(option, "id", optionId++);
			}
		}

		return quizSet;
	}

	private QuizSet createQuizSetWithMultipleQuizzes() {
		QuizSet newQuizSet = QuizSet.create(BOOK_ID, CHAPTER_ID);
		setField(newQuizSet, "id", QUIZ_SET_ID);
		setField(newQuizSet, "createdAt", LocalDateTime.now());

		List<Quiz> quizList = List.of(
			createQuizWithId(1L, "질문1", newQuizSet),
			createQuizWithId(2L, "질문2", newQuizSet),
			createQuizWithId(3L, "질문3", newQuizSet)
		);

		newQuizSet.addQuizList(quizList);
		return newQuizSet;
	}

	private Quiz createQuizWithId(Long id, String question, QuizSet quizSet) {
		Quiz quiz = Quiz.create(question, "설명", quizSet);
		setField(quiz, "id", id);

		List<QuizOption> options = List.of(
			QuizOption.create("옵션1", true, quiz),
			QuizOption.create("옵션2", false, quiz)
		);

		long optionId = 1L;
		for (QuizOption option : options) {
			setField(option, "id", optionId++);
		}

		quiz.addQuizOptionList(options);
		return quiz;
	}

	private SliceResponse<GetQuizSetPageResponse> createMockSliceResponse(int contentSize, Pageable pageable) {
		List<GetQuizSetPageResponse> content = Stream.iterate(1, i -> i + 1)
			.limit(contentSize)
			.map(i -> new GetQuizSetPageResponse("사용자" + i, (long)i, 10, 100, 4.5, 0.8, 300, LocalDateTime.now()))
			.toList();

		return SliceResponse.from(new SliceImpl<>(content, pageable, false));
	}

	private void assertQuizSetResponse(CreateQuizSetResponse response, int expectedQuizSize) {
		assertThat(response).isNotNull();
		assertThat(response.bookId()).isEqualTo(BOOK_ID);
		assertThat(response.chapterId()).isEqualTo(CHAPTER_ID);
		assertThat(response.quizResponseList()).hasSize(expectedQuizSize);
		assertThat(response.totalQuizCount()).isEqualTo(expectedQuizSize);
		assertThat(response.createdAt()).isNotNull();
	}
}