package com.readup.server.bookregistration.application;

import static com.readup.server.common.exception.ErrorCode.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.readup.server.bookregistration.domain.model.BookRegistration;
import com.readup.server.bookregistration.domain.repository.BookRegistrationRepository;
import com.readup.server.bookregistration.presentation.dto.CreateBookRegistrationRequest;
import com.readup.server.bookregistration.presentation.dto.CreateBookRegistrationResponse;
import com.readup.server.common.exception.ServiceException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookRegistrationService {

	private final BookRegistrationRepository bookRegistrationRepository;

	@Transactional
	public CreateBookRegistrationResponse createBookRegistration(Long userId, CreateBookRegistrationRequest request) {

		if (bookRegistrationRepository.existsByIsbn(request.isbn())) {
			throw new ServiceException(DUPLICATE_BOOK_REGISTRATION);
		}

		BookRegistration savedBookRegistration = bookRegistrationRepository.save(
			BookRegistration.create(userId, request.title(), request.isbn()));

		return CreateBookRegistrationResponse.from(savedBookRegistration);
	}
}
