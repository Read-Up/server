package com.readup.server.book.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.readup.server.book.domain.Book;
import com.readup.server.book.domain.Chapter;
import com.readup.server.book.domain.repository.BookRepository;
import com.readup.server.book.presentation.dto.UpdateChapterListRequest;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookCommandService {

	private final BookRepository bookRepository;

	@Transactional
	public void updateChapterList(UpdateChapterListRequest request) {
		Book book = bookRepository.getBookById(request.bookId());

		List<Chapter> chapterList = request.toChapterList();
		book.updateChapterList(chapterList);
	}
}
