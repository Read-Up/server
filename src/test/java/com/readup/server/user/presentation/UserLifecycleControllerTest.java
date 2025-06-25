package com.readup.server.user.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.readup.server.terms.domain.TermsCode;
import com.readup.server.user.application.UserLifecycleService;
import com.readup.server.user.dto.CreateUserRequest;
import com.readup.server.terms.dto.UserTermsConsentRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class UserLifecycleControllerTest {

    @InjectMocks
    private UserLifecycleController userLifecycleController;

    @Mock
    private UserLifecycleService userLifecycleService;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userLifecycleController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void 회원가입() throws Exception {
        // given
        CreateUserRequest request = new CreateUserRequest(
                List.of(new UserTermsConsentRequest(1L, TermsCode.SERVICE, true)),
                "테스트닉네임"
        );

        String json = objectMapper.writeValueAsString(request);

        doNothing().when(userLifecycleService).createUser(any(), any());

        // when & then
        mockMvc.perform(post("/private/users/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());
    }

    @Test
    void 회원삭제() throws Exception {
        doNothing().when(userLifecycleService).deleteUser(any());

        mockMvc.perform(delete("/private/users"))
                .andExpect(status().isOk());
    }
}
