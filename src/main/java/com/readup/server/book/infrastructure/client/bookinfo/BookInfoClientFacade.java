package com.readup.server.book.infrastructure.client.bookinfo;

import com.readup.server.book.infrastructure.client.bookinfo.vo.BookInfoVO;

public interface BookInfoClientFacade {

	BookInfoVO getBookInfo(String isbn);

	String getServiceName();
}
