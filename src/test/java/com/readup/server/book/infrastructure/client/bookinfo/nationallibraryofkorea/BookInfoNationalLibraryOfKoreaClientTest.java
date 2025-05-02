package com.readup.server.book.infrastructure.client.bookinfo.nationallibraryofkorea;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.readup.server.WireMockSupport;
import com.readup.server.book.application.client.vo.BookInfoVO;
import com.readup.server.common.exception.ErrorCode;
import com.readup.server.common.exception.FeignException;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BookInfoNationalLibraryOfKoreaClientTest extends WireMockSupport {

	@Autowired
	BookInfoNationalLibraryOfKoreaClient nationalLibraryOfKoreaClient;

	@Autowired
	NationalLibraryOfKoreaProperties nationalLibraryOfKoreaProperties;

	@Test
	@DisplayName("국립도서관 서비스 이름을 가져오는 테스트")
	void nationalLibraryOfKoreaClientGetServiceName() {
		//given
		String expectedServiceName = nationalLibraryOfKoreaProperties.getServiceName();

		//when
		String serviceName = nationalLibraryOfKoreaClient.getServiceName();

		//then
		assertEquals(expectedServiceName, serviceName);
	}

	@Nested
	@DisplayName("국립도서관 클라이언트에서 책 정보를 가져오는 테스트")
	class GetBookInfoTest {

		@Test
		@DisplayName("국립도서관 클라이언트에서 책 정보를 가져오기 성공 테스트")
		void nationalLibraryOfKoreaClientGetBookInfoSuccessTest() {
			//given
			String isbn = "9788966262281";

			//when
			BookInfoVO bookInfoVO = nationalLibraryOfKoreaClient.getBookInfo(isbn);

			//then
			assertEquals(isbn, bookInfoVO.isbn());
			assertNotNull(bookInfoVO.bookTitle());
			assertNotNull(bookInfoVO.author());
		}

		@Test
		@DisplayName("국립도서관 클라이언트에서 책 정보를 가져오기 실패 테스트 - 존재 하지 않는 책")
		void nationalLibraryOfKoreaClientGetBookInfo() {
			//given
			String isbn = "1234567890123";

			//when-then
			Throwable throwable = assertThrows(FeignException.class,
				() -> nationalLibraryOfKoreaClient.getBookInfo(isbn));
			assertEquals(ErrorCode.EXTERNAL_BOOK_INFO_NOT_FOUND.getMessage(), throwable.getMessage());
		}
	}
}