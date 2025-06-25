package com.readup.server.user.presentation;

import com.readup.server.auth.domain.SocialAccount;
import com.readup.server.common.annotaion.CurrentSocialAccount;
import com.readup.server.common.annotaion.CurrentUser;
import com.readup.server.user.domain.User;
import org.springframework.web.bind.annotation.*;

import com.readup.server.common.dto.ApiResponse;
import com.readup.server.user.application.UserLifecycleService;
import com.readup.server.user.dto.CreateUserRequest;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/private/users")
@RequiredArgsConstructor
public class UserLifecycleController {

	private final UserLifecycleService userLifecycleService;

    @PostMapping("/signup")
    public ApiResponse<Void> createUser(
            @CurrentSocialAccount SocialAccount socialAccount,
            @RequestBody CreateUserRequest request
    ) {
        userLifecycleService.createUser(socialAccount, request);
        return ApiResponse.successResponse();
    }

    @DeleteMapping
    public ApiResponse<Void> deleteUser(
            @CurrentUser User user
    ) {
        userLifecycleService.deleteUser(user);
        return ApiResponse.successResponse();
    }
}
