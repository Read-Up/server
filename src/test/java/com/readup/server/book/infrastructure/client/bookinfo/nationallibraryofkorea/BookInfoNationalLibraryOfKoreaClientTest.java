package com.readup.server.book.infrastructure.client.bookinfo.nationallibraryofkorea;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.readup.server.WireMockSupport;
import com.readup.server.book.application.client.vo.BookInfoVO;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BookInfoNationalLibraryOfKoreaClientTest extends WireMockSupport {

	@Autowired
	BookInfoNationalLibraryOfKoreaClient nationalLibraryOfKoreaClient;

	@Autowired
	NationalLibraryOfKoreaProperties nationalLibraryOfKoreaProperties;

	@Test
	@DisplayName("국립도서관 클라이언트에서 책 정보를 가져오는 테스트")
	void nationalLibraryOfKoreaClientGetBookInfo() {
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
	@DisplayName("국립도서관 서비스 이름을 가져오는 테스트")
	void nationalLibraryOfKoreaClientGetServiceName() {
		//given
		String expectedServiceName = nationalLibraryOfKoreaProperties.getServiceName();

		//when
		String serviceName = nationalLibraryOfKoreaClient.getServiceName();

		//then
		assertEquals(expectedServiceName, serviceName);
	}
}