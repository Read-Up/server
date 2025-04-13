package com.readup.server.bookregistration.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import com.readup.server.bookregistration.domain.model.BookRegistration;
import com.readup.server.bookregistration.domain.repository.BookRegistrationRepository;

public interface BookRegistrationJpaRepository
	extends BookRegistrationRepository, JpaRepository<BookRegistration, Long> {
	boolean existsByIsbn(String isbn);
}
