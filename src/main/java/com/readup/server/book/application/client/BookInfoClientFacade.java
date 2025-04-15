package com.readup.server.book.application.client;

import com.readup.server.book.application.client.vo.BookInfoVO;

public interface BookInfoClientFacade {

	BookInfoVO getBookInfo(String isbn);

	String getServiceName();
}
