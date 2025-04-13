package com.readup.server.bookregistration.stub;

import static com.readup.server.bookregistration.domain.model.RegistrationStatus.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import com.readup.server.bookregistration.domain.model.BookRegistration;
import com.readup.server.bookregistration.domain.repository.BookRegistrationRepository;

public class BookRegistrationJpaRepositoryStub implements BookRegistrationRepository {

	private final List<BookRegistration> bookRegistrationList = new ArrayList<>();
	private final AtomicLong idGenerator = new AtomicLong(1L);

	public void addBookRegistration(Long requesterId, String title, String isbn) {
		BookRegistration bookRegistrationWithId = createBookRegistrationWithId(requesterId, title, isbn);
		bookRegistrationList.add(bookRegistrationWithId);
	}

	@Override
	public BookRegistration save(BookRegistration bookRegistration) {
		BookRegistration bookRegistrationWithId = createBookRegistrationWithId(bookRegistration.getRequesterId(),
			bookRegistration.getTitle(), bookRegistration.getIsbn());

		bookRegistrationList.add(bookRegistrationWithId);

		return bookRegistrationWithId;
	}

	@Override
	public boolean existsByIsbn(String isbn) {
		return bookRegistrationList.stream()
			.anyMatch(bookRegistration -> isbn.equals(bookRegistration.getIsbn()));
	}

	private BookRegistration createBookRegistrationWithId(Long requesterId, String title, String isbn) {
		return BookRegistration.builder()
			.id(idGenerator.getAndIncrement())
			.requesterId(requesterId)
			.title(title)
			.isbn(isbn)
			.registrationStatus(PENDING)
			.build();
	}
}
