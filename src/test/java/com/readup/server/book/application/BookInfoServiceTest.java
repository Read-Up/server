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
import com.readup.server.book.application.client.vo.BookInfoVO;
import com.readup.server.book.domain.Book;
import com.readup.server.book.domain.repository.BookRepository;
import com.readup.server.book.presentation.dto.GetExternalBookResponse;
import com.readup.server.common.exception.ServiceException;

@ExtendWith(MockitoExtension.class)
class BookInfoServiceTest {

	@InjectMocks
	private BookInfoService bookInfoService;

	@Mock
	private BookInfoClientRegistry bookInfoClientRegistry;

	@Mock
	private BookInfoClientFacade bookInfoClientFacade;

	@Mock
	private BookRepository bookRepository;

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
				.chapterList(List.of())
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

			given(bookInfoClientRegistry.getDefaultBookInfoClient()).willReturn(bookInfoClientFacade);
			given(bookInfoClientFacade.getBookInfo(isbn)).willReturn(bookInfoVO);
			given(bookRepository.existsByIsbnOrTitle(anyString(), anyString())).willReturn(false);
			given(bookRepository.save(any(Book.class))).willReturn(savedBook);

			// when
			GetExternalBookResponse getExternalBookResponse = bookInfoService.getBookInfo(isbn);

			// then
			assertEquals(bookInfoVO.bookTitle(), getExternalBookResponse.bookTitle());
			assertEquals(bookInfoVO.publisher(), getExternalBookResponse.publisher());
			assertEquals(bookInfoVO.author(), getExternalBookResponse.author());
			assertEquals(isbn, getExternalBookResponse.isbn());
		}

		@Test
		@DisplayName("이미 존재하는 책 정보 가져오기 실패 테스트")
		void getBookInfoFailWhenBookAlreadyExits() {
			// given
			String isbn = "9788960773417";
			BookInfoVO bookInfoVO = BookInfoVO.builder()
				.bookTitle("토비의 스프링 3.1 Vol. 1 스프링의 이해와 원리")
				.publisher("에이콘출판(주)")
				.author("이일민")
				.isbn(isbn)
				.titleUrl("http://www.nl.go.kr/seoji/fu/ecip/dbfiles/CIP_FILES_TBL/2577606_3.jpg")
				.chapterList(List.of())
				.build();

			given(bookInfoClientRegistry.getDefaultBookInfoClient()).willReturn(bookInfoClientFacade);
			given(bookInfoClientFacade.getBookInfo(isbn)).willReturn(bookInfoVO);
			given(bookRepository.existsByIsbnOrTitle(anyString(), anyString())).willReturn(true);

			// when-then
			assertThrows(ServiceException.class, () -> bookInfoService.getBookInfo(isbn));
		}
	}
}