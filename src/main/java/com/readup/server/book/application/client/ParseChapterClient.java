package com.readup.server.book.application.client;

import java.util.List;

import com.readup.server.book.application.client.vo.ChapterVO;

public interface ParseChapterClient {
	List<ChapterVO> parseRawChapter(String rawChapter);
}
