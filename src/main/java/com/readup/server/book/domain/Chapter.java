package com.readup.server.book.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "chapter")
public class Chapter {
	@Id
	Long id;

	@Column(name = "name", nullable = false)
	String name;

	@Column(name = "chapter_number", nullable = false)
	Integer chapterNumber;

	@ManyToOne
	@JoinColumn(name = "book_id", nullable = false)
	Book book;
}
