package com.readup.server.user;

import com.readup.server.user.dto.AuthUser;
import com.readup.server.user.domain.User;

public class AuthUserTestBuilder {
    public static AuthUser from(User user) {
        return new AuthUser(user.getId(), user.getNickname());
    }
}
