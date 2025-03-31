package com.readup.server.book.presentation;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.readup.server.book.application.BookCommandService;
import com.readup.server.book.presentation.dto.UpdateChapterListRequest;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/books")
public class BookController {

	private final BookCommandService bookCommandService;

	@PutMapping("/{bookId}/chapters")
	public void updateChapterList(@PathVariable Long bookId, @RequestBody UpdateChapterListRequest request) {
		bookCommandService.updateChapterList(request);
	}
}
