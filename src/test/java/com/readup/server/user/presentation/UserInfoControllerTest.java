// UserInfoControllerTest.java
package com.readup.server.user.presentation;

import com.readup.server.common.dto.ApiResponse;
import com.readup.server.user.UserTestBuilder;
import com.readup.server.user.application.RandomNicknameService;
import com.readup.server.user.application.UserInfoService;
import com.readup.server.user.domain.User;
import com.readup.server.user.dto.UpdateUserRequest;
import com.readup.server.user.dto.UserResponse;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class UserInfoControllerTest {

    @Mock
    private UserInfoService userInfoService;

    @Mock
    private RandomNicknameService randomNicknameService;

    @InjectMocks
    private UserInfoController userInfoController;

    private final MockMvc mockMvc;

    public UserInfoControllerTest() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(userInfoController).build();
    }

    @Test
    void 유저정보조회_API_정상처리() throws Exception {
        // given
        User user = UserTestBuilder.builder()
                .id(1L)
                .nickname("테스트닉")
                .build();

        UserResponse expectedResponse = UserResponse.from(user);

        when(userInfoService.getUser(any())).thenReturn(expectedResponse);

        // when & then
        mockMvc.perform(get("/private/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.nickname").value("테스트닉"));
    }

    @Test
    void 랜덤닉네임_API_정상처리() throws Exception {
        when(randomNicknameService.generate()).thenReturn("창의적인탐험가");

        mockMvc.perform(get("/public/users/random-nickname"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value("창의적인탐험가"));
    }

    @Test
    void 유저수정_API_정상처리() throws Exception {
        // given
        MockMultipartFile image = new MockMultipartFile("image", "test.jpg", "image/jpeg", "dummy".getBytes());
        MockMultipartFile json = new MockMultipartFile("request", "", "application/json", "{\"nickname\":\"바뀐닉\"}".getBytes());

        User updatedUser = UserTestBuilder.builder()
                .id(1L)
                .nickname("바뀐닉")
                .build();

        when(userInfoService.updateUser(any(), any(), any()))
                .thenReturn(UserResponse.from(updatedUser));

        // when & then
        mockMvc.perform(multipart("/private/users")
                        .file(json)
                        .file(image)
                        .with(req -> {
                            req.setMethod("PATCH"); // PATCH override
                            return req;
                        })
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.nickname").value("바뀐닉"));
    }
}
