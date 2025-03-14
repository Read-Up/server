package com.readup.server.auth.application;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.stereotype.Service;

@Service
public class CustomOAuthUserService extends DefaultOAuth2UserService {
}
