package com.readup.server.book.presentation;

import static com.readup.server.common.dto.ApiResponse.*;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.readup.server.book.application.BookInfoService;
import com.readup.server.book.presentation.dto.GetExternalBookResponse;
import com.readup.server.common.dto.ApiResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/public/external-books")
public class BookInfoController {

	private final BookInfoService bookInfoService;

	@GetMapping("/{isbn}")
	public ApiResponse<GetExternalBookResponse> getBookInfo(@PathVariable String isbn) {

		GetExternalBookResponse response = bookInfoService.getBookInfo(isbn);

		return successResponse(response);
	}
}
