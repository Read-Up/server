package com.readup.server.user.generator;

import java.util.List;
import java.util.Random;

public class RandomNicknameGenerator {

  public static final List<String> ADJECTIVES = List.of("지적인", "창의적인", "철학적인", "문제푸는", "논리적인", "질문많은", "박식한", "책을사랑하는", "몰입하는", "감성깊은");
  public static final List<String> NOUNS = List.of("독서가", "분석가", "탐험가", "개발자", "문학가", "철학자", "지식인", "서재인", "퀴즈왕", "이야기꾼");

  private static final Random RANDOM = new Random();

  private RandomNicknameGenerator() {}

  public static String generate() {
    int adjectiveIndex = RANDOM.nextInt(ADJECTIVES.size());
    int nounIndex = RANDOM.nextInt(NOUNS.size());
    return ADJECTIVES.get(adjectiveIndex) + NOUNS.get(nounIndex);
  }
}
