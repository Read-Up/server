package com.readup.server.user.presentation;

import com.readup.server.common.annotaion.CurrentUser;
import com.readup.server.common.dto.ApiResponse;
import com.readup.server.user.application.UserInfoService;
import com.readup.server.user.domain.User;
import com.readup.server.user.dto.UpdateUserRequest;
import com.readup.server.user.dto.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static com.readup.server.common.dto.ApiResponse.successResponse;
import static com.readup.server.user.generator.RandomNicknameGenerator.generate;


@RestController
@RequiredArgsConstructor
public class UserInfoController {

    private final UserInfoService userInfoService;

    @PatchMapping("/private/users")
    public ApiResponse<UserResponse> updateUser(
            @CurrentUser User user,
            @RequestBody UpdateUserRequest request
    ) {
        return ApiResponse.successResponse(userInfoService.updateUser(user, request));
    }

    @GetMapping("/private/users")
    public ApiResponse<UserResponse> getUser(
            @CurrentUser User user
    ) {
        return ApiResponse.successResponse(userInfoService.getUser(user));
    }

    @GetMapping("/public/users/random-nickname")
    public ApiResponse<String> getRandomNickname() {
        return successResponse(generate());
    }
}
