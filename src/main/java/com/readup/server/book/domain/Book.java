package com.readup.server.book.domain;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "book")
@NoArgsConstructor
public class Book {
	@Id
	Long id;

	@Column(name = "title", nullable = false)
	String title;

	@Column(name = "author", nullable = false)
	String author;

	@Column(name = "publisher", nullable = false)
	String publisher;

	@Column(name = "isbn", nullable = false)
	String isbn;

	@Column(name = "title_url", nullable = false)
	String titleUrl;

	@Column(name = "summary", nullable = false)
	String summary;

	@OneToMany(mappedBy = "book", fetch = FetchType.EAGER)
	List<Chapter> chapterList;
}
