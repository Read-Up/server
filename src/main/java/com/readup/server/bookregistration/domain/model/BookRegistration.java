package com.readup.server.bookregistration.domain.model;

import static com.readup.server.bookregistration.domain.model.RegistrationStatus.*;
import static lombok.AccessLevel.*;

import com.readup.server.common.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Builder
@AllArgsConstructor(access = PRIVATE)
@NoArgsConstructor(access = PROTECTED)
public class BookRegistration extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private Long requesterId;

	@Column
	private Long processorId;

	@Column(nullable = false)
	private String title;

	@Column(nullable = false, unique = true)
	private String isbn;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private RegistrationStatus registrationStatus;

	public static BookRegistration create(Long requesterId, String title, String isbn) {
		return BookRegistration.builder()
			.requesterId(requesterId)
			.title(title)
			.isbn(isbn)
			.registrationStatus(PENDING)
			.build();
	}
}
