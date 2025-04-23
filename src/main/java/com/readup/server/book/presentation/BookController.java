package com.readup.server.book.presentation;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.readup.server.book.application.BookCommandService;
import com.readup.server.book.application.BookQueryService;
import com.readup.server.book.application.dto.GetBookResponse;
import com.readup.server.book.application.dto.SearchBookRequest;
import com.readup.server.book.application.dto.UpdateChapterListRequest;
import com.readup.server.common.dto.ApiResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/public/books")
public class BookController {

	private final BookCommandService bookCommandService;
	private final BookQueryService bookQueryService;

	@GetMapping
	public ApiResponse<PagedModel<GetBookResponse>> searchBook(SearchBookRequest searchBookRequest,
		@PageableDefault Pageable pageable) {

		PagedModel<GetBookResponse> getBookResponsePagedModel =
			bookQueryService.searchBook(searchBookRequest, pageable);

		return ApiResponse.successResponse(getBookResponsePagedModel);
	}

	@PutMapping("/{bookId}/chapters")
	public ApiResponse<Void> updateChapterList(@PathVariable Long bookId,
		@RequestBody UpdateChapterListRequest request) {

		bookCommandService.updateChapterList(bookId, request);

		return ApiResponse.successResponse();
	}
}
