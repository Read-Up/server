package com.readup.server.book.infrastructure.client.gemini.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.readup.server.book.infrastructure.client.gemini.dto.GeminiRequest;
import com.readup.server.book.infrastructure.client.gemini.dto.GeminiResponse;

@FeignClient(name = "gemini", url = "https://generativelanguage.googleapis.com")
public interface GeminiFeignClient {

	@PostMapping("/v1beta/models/gemini-2.5-flash-preview-04-17:generateContent")
	GeminiResponse getGeminiResponse(@RequestParam String key, @RequestBody GeminiRequest geminiRequest);

}
