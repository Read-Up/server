package com.readup.server.bookregistration.domain.model;

import static com.readup.server.bookregistration.domain.model.RegistrationStatus.*;
import static lombok.AccessLevel.*;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import com.readup.server.common.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Builder
@Table(name = "book_registration")
@SQLRestriction("deleted_at IS NULL")
@SQLDelete(sql = "UPDATE book_registration SET deleted_at = NOW() WHERE id = ?")
@AllArgsConstructor(access = PRIVATE)
@NoArgsConstructor(access = PROTECTED)
public class BookRegistration extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "requester_id", nullable = false)
	private Long requesterId;

	@Column(name = "processor_id")
	private Long processorId;

	@Column(name = "title", nullable = false)
	private String title;

	@Column(name = "isbn", nullable = false, unique = true)
	private String isbn;

	@Column(name = "registration_status", nullable = false)
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
