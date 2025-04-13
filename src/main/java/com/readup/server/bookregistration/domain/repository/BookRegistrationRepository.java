package com.readup.server.bookregistration.domain.repository;

import com.readup.server.bookregistration.domain.model.BookRegistration;

public interface BookRegistrationRepository {
	BookRegistration save(BookRegistration bookRegistration);

	boolean existsByIsbn(String isbn);
}
