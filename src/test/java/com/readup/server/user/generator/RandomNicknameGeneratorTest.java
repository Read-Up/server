package com.readup.server.user.generator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RandomNicknameGeneratorTest {

  @Test
  @DisplayName("generate: 랜덤 닉네임 생성 메서드")
  void generate_success() {
  	// when
    String generatedRandomNickname = RandomNicknameGenerator.generate();

    boolean isAdjectiveContained = RandomNicknameGenerator.ADJECTIVES.stream()
        .anyMatch(generatedRandomNickname::startsWith);

    boolean isNounContained = RandomNicknameGenerator.NOUNS.stream()
        .anyMatch(generatedRandomNickname::endsWith);

    // then
    assertNotNull(generatedRandomNickname);
    assertTrue(isAdjectiveContained);
    assertTrue(isNounContained);
    assertThat(generatedRandomNickname.length()).isBetween(2, 12);
  }
}