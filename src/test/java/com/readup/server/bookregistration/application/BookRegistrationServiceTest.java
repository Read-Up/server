package com.readup.server.bookregistration.application;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import com.readup.server.bookregistration.domain.model.BookRegistration;
import com.readup.server.bookregistration.presentation.dto.CreateBookRegistrationRequest;
import com.readup.server.bookregistration.presentation.dto.CreateBookRegistrationResponse;
import com.readup.server.bookregistration.stub.BookRegistrationJpaRepositoryStub;
import com.readup.server.common.exception.ServiceException;

@ExtendWith(MockitoExtension.class)
class BookRegistrationServiceTest {

	@InjectMocks
	private BookRegistrationService sut;

	@Spy
	private BookRegistrationJpaRepositoryStub bookRegistrationJpaRepository;

	@Nested
	class CreateBookRegistration {

		@BeforeEach
		void setUp() {
			final Long requesterId = 1L;
			final String title = "함께 자라기";
			final String existingIsbn = "9788966262335";
			bookRegistrationJpaRepository.addBookRegistration(requesterId, title, existingIsbn);
		}

		@Test
		@DisplayName("책 등록 요청 성공")
		void createBookRegistration_success() {
			// given
			final Long requesterId = 1L;
			final String title = "도메인 주도 개발 시작하기";
			final String nonExistingIsbn = "9791162245385";

			final CreateBookRegistrationRequest request = new CreateBookRegistrationRequest(title, nonExistingIsbn);

			// when
			final CreateBookRegistrationResponse response = sut.createBookRegistration(requesterId, request);

			// then
			assertAll(
				() -> assertNotNull(response),
				() -> assertEquals(requesterId, response.requesterId()),
				() -> assertEquals(title, response.title()),
				() -> assertEquals(nonExistingIsbn, response.isbn()));
		}

		@Test
		@DisplayName("이미 존재하는 책 등록 요청 시 실패")
		void createBookRegistration_duplicate_request() {
			// given
			final Long requesterId = 1L;
			final String title = "함께 자라기";
			final String existingIsbn = "9788966262335";

			final CreateBookRegistrationRequest request = new CreateBookRegistrationRequest(title, existingIsbn);

			// when & then
			assertThrows(ServiceException.class, () -> sut.createBookRegistration(requesterId, request));

			verify(bookRegistrationJpaRepository).existsByIsbn(existingIsbn);
			verify(bookRegistrationJpaRepository, never()).save(any(BookRegistration.class));
		}
	}
}