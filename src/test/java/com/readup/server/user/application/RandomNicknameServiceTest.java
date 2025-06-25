package com.readup.server.user.application;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import static org.assertj.core.api.Assertions.assertThat;

class RandomNicknameServiceTest {

    @InjectMocks
    private RandomNicknameService nicknameService;

    @Test
    void 랜덤닉네임은_형용사와_명사를_포함한다() {
        // when
        String nickname = nicknameService.generate();

        // then
        assertThat(nickname).isNotBlank();
        assertThat(nickname.length()).isGreaterThan(3);
    }

    @Test
    void 여러번_호출해도_중복되지_않을수도_있다() {
        // when
        String first = nicknameService.generate();
        String second = nicknameService.generate();

        // then
        assertThat(first).isNotNull();
        assertThat(second).isNotNull();
    }
}
