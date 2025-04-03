package com.readup.server.bookregistration.presentation.dto;

import com.readup.server.bookregistration.domain.model.BookRegistration;
import com.readup.server.bookregistration.domain.model.RegistrationStatus;

public record CreateBookRegistrationResponse(Long id, Long requesterId, String title, String isbn,
											 RegistrationStatus registrationStatus) {

	public static CreateBookRegistrationResponse from(BookRegistration registration) {
		return new CreateBookRegistrationResponse(registration.getId(), registration.getRequesterId(),
			registration.getTitle(), registration.getIsbn(), registration.getRegistrationStatus());
	}
}
