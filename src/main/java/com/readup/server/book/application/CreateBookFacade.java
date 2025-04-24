package com.readup.server.book.application;

import java.util.List;

import org.springframework.stereotype.Service;

import com.readup.server.book.application.client.BookInfoClientRegistry;
import com.readup.server.book.application.client.ParseChapterClient;
import com.readup.server.book.application.client.vo.BookInfoVO;
import com.readup.server.book.application.client.vo.ChapterVO;
import com.readup.server.book.application.dto.RetrieveBookResponse;
import com.readup.server.book.domain.Book;
import com.readup.server.book.domain.service.BookDomainService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CreateBookFacade {
	private final BookDomainService bookDomainService;
	private final BookInfoClientRegistry bookInfoClientRegistry;
	private final ParseChapterClient parseChapterClient;

	public RetrieveBookResponse createBook(String isbn) {

		bookDomainService.doesNotExistBookByIsbn(isbn);

		BookInfoVO bookInfoVO = bookInfoClientRegistry.getDefaultBookInfoClient().getBookInfo(isbn);
		List<ChapterVO> chapterVO = parseChapterClient.parseRawChapter(bookInfoVO.rawChapter());

		Book book = bookInfoVO.toEntity();
		book.updateChapterList(chapterVO.stream().map(ChapterVO::toEntity).toList());

		Book savedBook = bookDomainService.save(book);

		return RetrieveBookResponse.from(savedBook);
	}
}
