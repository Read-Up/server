package com.readup.server.book.application;

import java.util.List;

import org.springframework.stereotype.Service;

import com.readup.server.book.domain.Book;
import com.readup.server.book.domain.Chapter;
import com.readup.server.book.infrastructure.repository.BookJpaRepository;
import com.readup.server.book.presentation.dto.UpdateChapterListRequest;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookCommandService {

	private final BookJpaRepository bookJpaRepository;

	public void updateChapterList(UpdateChapterListRequest request) {
		Book book = bookJpaRepository.getBookById(request.bookId());

		List<Chapter> chapterList = request.toChapterList();
		book.updateChapterList(chapterList);
	}
}
