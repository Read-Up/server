package com.readup.server.book.domain;

import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;

import com.readup.server.common.exception.DomainException;
import com.readup.server.common.exception.ErrorCode;

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

	@Column(name = "title", nullable = false, unique = true)
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

	public void updateChapterList(List<Chapter> chapterList) {
		List<Chapter> sortedChapterList = chapterList.stream()
			.sorted(Comparator.comparing(Chapter::getChapterNumber))
			.toList();

		validateChapterOrder(sortedChapterList);

		this.chapterList = sortedChapterList;
	}

	private void validateChapterOrder(List<Chapter> chapters) {
		IntStream.range(0, chapters.size())
			.forEach(i -> {
				int expected = i + 1;
				int actual = chapters.get(i).getChapterNumber();
				if (actual != expected) {
					throw new DomainException(ErrorCode.INVALID_CHAPTER_NUMBER);
				}
			});
	}
}
