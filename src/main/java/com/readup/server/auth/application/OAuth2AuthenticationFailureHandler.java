package com.readup.server.auth.application;

import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {
}
