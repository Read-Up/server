package com.readup.server.book.application;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.readup.server.book.application.client.BookInfoClientFacade;
import com.readup.server.book.application.client.BookInfoClientRegistry;
import com.readup.server.book.application.client.ParseChapterClient;
import com.readup.server.book.application.client.vo.BookInfoVO;
import com.readup.server.book.application.client.vo.ChapterVO;
import com.readup.server.book.application.dto.RetrieveBookResponse;
import com.readup.server.book.domain.Book;
import com.readup.server.book.domain.service.BookDomainService;
import com.readup.server.common.exception.DomainException;
import com.readup.server.common.exception.ErrorCode;

@ExtendWith(MockitoExtension.class)
class CreateBookFacadeTest {

	@InjectMocks
	private CreateBookFacade createBookFacade;

	@Mock
	private BookInfoClientRegistry bookInfoClientRegistry;

	@Mock
	private BookInfoClientFacade bookInfoClientFacade;

	@Mock
	private ParseChapterClient parseChapterClient;

	@Mock
	private BookDomainService bookDomainService;

	@Nested
	@DisplayName("책 정보 가져오기 테스트")
	class GetBookInfoSuccess {

		@Test
		@DisplayName("책 정보 가져오기 성공 테스트")
		void getBookInfoSuccessTest() {
			// given
			String isbn = "9788960773417";
			BookInfoVO bookInfoVO = BookInfoVO.builder()
				.bookTitle("토비의 스프링 3.1 Vol. 1 스프링의 이해와 원리")
				.publisher("에이콘출판(주)")
				.author("이일민")
				.isbn(isbn)
				.titleUrl("http://www.nl.go.kr/seoji/fu/ecip/dbfiles/CIP_FILES_TBL/2577606_3.jpg")
				.rawChapter("1장 소개, 2장 설치와 설정")
				.build();

			Book savedBook = Book.builder()
				.id(1L)
				.title(bookInfoVO.bookTitle())
				.publisher(bookInfoVO.publisher())
				.author(bookInfoVO.author())
				.isbn(isbn)
				.titleUrl(bookInfoVO.titleUrl())
				.chapterList(List.of())
				.build();

			List<ChapterVO> chapterVOList = List.of(
				ChapterVO.builder()
					.name("1장 소개")
					.chapterOrder(1)
					.build(),
				ChapterVO.builder()
					.name("2장 설치와 설정")
					.chapterOrder(2)
					.build()
			);

			given(bookInfoClientRegistry.getDefaultBookInfoClient()).willReturn(bookInfoClientFacade);
			given(bookInfoClientFacade.getBookInfo(isbn)).willReturn(bookInfoVO);
			given(bookDomainService.save(any(Book.class))).willReturn(savedBook);
			given(parseChapterClient.parseRawChapter(anyString())).willReturn(chapterVOList);
			willDoNothing().given(bookDomainService).doesNotExistBookByIsbn(anyString());

			// when
			RetrieveBookResponse retrieveBookResponse = createBookFacade.createBook(isbn);

			// then
			assertEquals(bookInfoVO.bookTitle(), retrieveBookResponse.bookTitle());
			assertEquals(bookInfoVO.publisher(), retrieveBookResponse.publisher());
			assertEquals(bookInfoVO.author(), retrieveBookResponse.author());
			assertEquals(isbn, retrieveBookResponse.isbn());
		}

		@Test
		@DisplayName("이미 존재하는 책 정보 가져오기 실패 테스트")
		void getBookInfoFailWhenBookAlreadyExits() {
			// given
			String isbn = "9788960773417";

			willThrow(new DomainException(ErrorCode.DUPLICATE_BOOK)).given(bookDomainService)
				.doesNotExistBookByIsbn(anyString());

			// when-then
			assertThrows(DomainException.class, () -> createBookFacade.createBook(isbn));
		}
	}
}