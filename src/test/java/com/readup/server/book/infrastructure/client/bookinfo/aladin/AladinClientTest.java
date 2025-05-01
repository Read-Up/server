package com.readup.server.book.infrastructure.client.bookinfo.aladin;

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
class AladinClientTest extends WireMockSupport {

	@Autowired
	AladinClient aladinClient;

	@Autowired
	AladinProperties aladinProperties;

	@Test
	@DisplayName("알라딘 서비스 이름을 가져오는 테스트")
	void getServiceName() {
		//given
		String expectedServiceName = aladinProperties.getServiceName();

		//when
		String serviceName = aladinClient.getServiceName();

		//then
		assertEquals(expectedServiceName, serviceName);
	}

	@Nested
	@DisplayName("알라딘 클라이언트에서 책 정보를 가져오는 테스트")
	class getBookInfoTest {

		@Test
		@DisplayName("알라딘 클라이언트에서 책 정보를 가져오기 성공 테스트")
		void getBookInfoSuccessTest() {
			//given
			String isbn = "9788966262281";

			//when
			BookInfoVO bookInfoVO = aladinClient.getBookInfo(isbn);

			//then
			assertEquals(isbn, bookInfoVO.isbn());
			assertNotNull(bookInfoVO.bookTitle());
			assertNotNull(bookInfoVO.author());
		}

		@Test
		@DisplayName("알라딘 클라이언트에서 책 정보를 가져오기 실패 테스트 - 존재 하지 않는 책")
		void getBookInfoFailWhenBookDoseNotExistTest() {
			//given
			String isbn = "1234567890123";

			//when-then
			Throwable throwable = assertThrows(FeignException.class, () -> aladinClient.getBookInfo(isbn));
			assertEquals(ErrorCode.EXTERNAL_BOOK_INFO_NOT_FOUND.getMessage(), throwable.getMessage());
		}

		@Test
		@DisplayName("알라딘 클라이언트에서 책 정보를 가져오기 실패 테스트 - 알라딘 API 서버 에러")
		void getBookInfoFailWhenAladinApiServerErrorTest() {
			//given
			String isbn = "1234567890124";

			//when-then
			Throwable throwable = assertThrows(FeignException.class, () -> aladinClient.getBookInfo(isbn));
			assertEquals(ErrorCode.API_SERVER_ERROR.getMessage(), throwable.getMessage());
		}
	}
}