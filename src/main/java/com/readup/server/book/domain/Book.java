package com.readup.server.book.domain;

import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import com.readup.server.common.entity.BaseEntity;
import com.readup.server.common.exception.DomainException;
import com.readup.server.common.exception.ErrorCode;

import jakarta.persistence.CascadeType;
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
@SQLRestriction("deleted_at IS NULL")
@SQLDelete(sql = "UPDATE book SET deleted_at = NOW() WHERE id = ?")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Book extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "title", nullable = false)
	private String title;

	@Column(name = "author", nullable = false)
	private String author;

	@Column(name = "publisher", nullable = false)
	private String publisher;

	@Column(name = "isbn", nullable = false, unique = true)
	private String isbn;

	@Column(name = "title_url")
	private String titleUrl;

	@Column(name = "summary")
	private String summary;

	@OneToMany(mappedBy = "book", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Chapter> chapterList;

	public void updateChapterList(List<Chapter> chapterList) {
		List<Chapter> sortedChapterList = chapterList.stream()
			.sorted(Comparator.comparing(Chapter::getChapterNumber))
			.toList();

		validateChapterOrder(sortedChapterList);

		this.chapterList.clear();
		this.chapterList.addAll(sortedChapterList);
		this.chapterList.forEach(chapter -> chapter.updateBook(this));
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
