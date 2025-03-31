package com.readup.server.book.domain;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "book")
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor
public class Book {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;

	@Column(name = "title", nullable = false)
	String title;

	@Column(name = "author", nullable = false)
	String author;

	@Column(name = "publisher", nullable = false)
	String publisher;

	@Column(name = "isbn", nullable = false, unique = true)
	String isbn;

	@Column(name = "title_url")
	String titleUrl;

	@Column(name = "summary")
	String summary;

	@OneToMany(mappedBy = "book", fetch = FetchType.EAGER)
	List<Chapter> chapterList;
}
