package com.readup.server.book.application.client;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BookInfoClientRegistryTest {

	@Mock
	private BookInfoClientProperties properties;

	@Mock
	private BookInfoClientFacade nationalLibraryOfKorea;

	@Mock
	private BookInfoClientFacade aladdin;

	private BookInfoClientRegistry registry;

	@BeforeEach
	void setUp() {
		given(nationalLibraryOfKorea.getServiceName()).willReturn("NationalLibraryOfKorea");
		given(aladdin.getServiceName()).willReturn("Aladdin");

		registry = new BookInfoClientRegistry(List.of(nationalLibraryOfKorea, aladdin), properties);
		registry.init(); // manually trigger @PostConstruct
	}

	@Nested
	@DisplayName("책정보 클라이언트 조회 테스트")
	class GetBookInfoClient {

		@Test
		@DisplayName("책정보 클라이언트 조회 성공 테스트")
		void shouldReturnClientWhenServiceNameIsRegisteredTest() {
			// when
			BookInfoClientFacade client = registry.getBookInfoClient("NationalLibraryOfKorea");

			// then
			assertEquals("NationalLibraryOfKorea", client.getServiceName());
		}

		@Test
		@DisplayName("책 정보 조회 실패 테스트 - 존재하지 않는 클라이언트 이름")
		void shouldThrowExceptionWhenServiceNameIsNotRegisteredTest() {
			// when & then
			IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
				() -> registry.getBookInfoClient("unknown"));

			assertThat(exception.getMessage()).contains("Not supported service name");
		}
	}

	@Nested
	@DisplayName("기본 책정보 클라이언트 조회 테스트")
	class GetDefaultBookInfoClient {

		@Test
		@DisplayName("기본 책정보 클라이언트 조회 성공 테스트")
		void shouldReturnDefaultClientFromPropertiesTest() {
			//given
			given(properties.getDefaultServiceName()).willReturn("NationalLibraryOfKorea");
			
			// when
			BookInfoClientFacade defaultClient = registry.getDefaultBookInfoClient();

			// then
			assertEquals(nationalLibraryOfKorea, defaultClient);
		}
	}
}