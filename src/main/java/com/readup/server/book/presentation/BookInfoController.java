package com.readup.server.book.presentation;

import static com.readup.server.common.dto.ApiResponse.*;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.readup.server.book.application.CreateBookFacade;
import com.readup.server.book.application.dto.RetrieveBookResponse;
import com.readup.server.common.dto.ApiResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/public/external-books")
public class BookInfoController {

	private final CreateBookFacade createBookFacade;

	@PostMapping("/{isbn}")
	public ApiResponse<RetrieveBookResponse> getBookInfo(@PathVariable String isbn) {

		RetrieveBookResponse response = createBookFacade.createBook(isbn);

		return successResponse(response);
	}
}
