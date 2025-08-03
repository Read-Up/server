package com.readup.server.user.application;

import com.readup.server.auth.domain.SocialAccount;
import com.readup.server.auth.domain.SocialAccountService;
import com.readup.server.terms.application.TermsManagementService;
import com.readup.server.terms.domain.UserTermsConsent;
import com.readup.server.user.domain.User;
import com.readup.server.user.domain.repository.UserRepository;
import com.readup.server.user.dto.CreateUserRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;

import static org.mockito.Mockito.*;

class UserLifecycleServiceTest {

    @InjectMocks
    private UserLifecycleService userLifecycleService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private SocialAccountService socialAccountService;

    @Mock
    private TermsManagementService termsManagementService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createUser_회원가입() {
        // given
        SocialAccount socialAccount = mock(SocialAccount.class);
        CreateUserRequest request = new CreateUserRequest(List.of(),"닉네임");

        List<UserTermsConsent> consentList = List.of(mock(UserTermsConsent.class));
        when(termsManagementService.createUserTermsConsent(request)).thenReturn(consentList);
        when(socialAccountService.getById(any())).thenReturn(socialAccount);

        // when
        userLifecycleService.createUser(socialAccount, request);

        // then
        verify(termsManagementService).createUserTermsConsent(request);
        verify(userRepository).save(any(User.class));
    }

    @Test
    void deleteUser_회원삭제() {
        // given
        User user = mock(User.class);

        // when
        userLifecycleService.deleteUser(user);

        // then
        verify(userRepository).delete(user);
    }
}
