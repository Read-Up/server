package com.readup.server.user.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class RandomNicknameService {

	private static final List<String> ADJECTIVES = List.of(
		"지적인", "창의적인", "철학적인", "문제푸는", "논리적인", "질문많은", "박식한", "책을사랑하는", "몰입하는", "감성깊은");
	private static final List<String> NOUNS = List.of(
		"독서가", "분석가", "탐험가", "개발자", "문학가", "철학자", "지식인", "서재인", "퀴즈왕", "이야기꾼");

	private final Random random = new Random();

	public String generate() {
		String adjective = ADJECTIVES.get(random.nextInt(ADJECTIVES.size()));
		String noun = NOUNS.get(random.nextInt(NOUNS.size()));
		return adjective + noun;
	}
}
