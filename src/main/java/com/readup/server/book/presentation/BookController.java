package com.readup.server.book.presentation;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.readup.server.book.presentation.dto.UpdateChapterListRequest;

@RestController
@RequestMapping("/books")
public class BookController {

	@GetMapping
	public void updateChapter(@RequestBody UpdateChapterListRequest request) {
	}
}
