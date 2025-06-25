package com.readup.server.user.application;

import com.readup.server.auth.domain.SocialAccount;
import com.readup.server.common.util.R2Uploader;
import com.readup.server.user.UserTestBuilder;
import com.readup.server.user.domain.User;
import com.readup.server.user.domain.repository.UserRepository;
import com.readup.server.user.dto.UpdateUserRequest;
import com.readup.server.user.dto.UserResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.web.multipart.MultipartFile;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserInfoServiceTest {

    @InjectMocks
    private UserInfoService userInfoService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private R2Uploader r2Uploader;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void updateUser_닉네임변경() {
        // given
        SocialAccount account = mock(SocialAccount.class);
        when(account.getEmail()).thenReturn("test@email.com");

        User user = mock(User.class);
        when(user.getId()).thenReturn(1L);
        when(user.getNickname()).thenReturn("기존닉네임");
        when(user.getImageUrl()).thenReturn("기존_URL");
        when(user.getSocialAccount()).thenReturn(account);

        UpdateUserRequest request = new UpdateUserRequest("새닉네임");

        when(userRepository.save(any(User.class))).thenReturn(user);

        // when
        UserResponse response = userInfoService.updateUser(user, request, null);

        // then
        verify(r2Uploader, never()).uploadSingle(any(), any(), any());
        verify(user).update("새닉네임", "기존_URL");
        assertThat(response).isNotNull();
    }

    @Test
    void updateUser_닉네임_이미지_모두_변경() throws Exception {
        // given
        SocialAccount account = mock(SocialAccount.class);
        when(account.getEmail()).thenReturn("test@email.com");

        User user = mock(User.class);
        when(user.getId()).thenReturn(1L);
        when(user.getNickname()).thenReturn("기존닉네임");
        when(user.getImageUrl()).thenReturn("기존_URL");
        when(user.getSocialAccount()).thenReturn(account);


        MultipartFile file = mock(MultipartFile.class);
        when(file.isEmpty()).thenReturn(false);

        UpdateUserRequest request = new UpdateUserRequest("새닉네임");
        when(r2Uploader.uploadSingle(file, 1L, "profile")).thenReturn("새_URL");
        when(user.getId()).thenReturn(1L);
        when(userRepository.save(any(User.class))).thenReturn(user);

        // when
        UserResponse response = userInfoService.updateUser(user, request, file);

        // then
        verify(r2Uploader).uploadSingle(file, 1L, "profile");
        verify(user).update("새닉네임", "새_URL");
        assertThat(response).isNotNull();
    }

    @Test
    void getUser_유저정보조회() {
        // given
        SocialAccount account = mock(SocialAccount.class);
        when(account.getEmail()).thenReturn("test@email.com");

        User user = mock(User.class);
        when(user.getId()).thenReturn(1L);
        when(user.getNickname()).thenReturn("기존닉네임");
        when(user.getImageUrl()).thenReturn("기존_URL");
        when(user.getSocialAccount()).thenReturn(account);

        // when
        UserResponse response = userInfoService.getUser(user);

        // then
        assertThat(response).isNotNull();
        assertThat(response.nickname()).isEqualTo("기존닉네임");
        assertThat(response.imageUrl()).isEqualTo("기존_URL");
    }
}
