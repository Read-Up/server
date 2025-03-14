package com.readup.server.auth.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.readup.server.auth.dto.OAuthLoginRequest;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.io.IOException;

public class OauthAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private final ObjectMapper objectMapper;

    public OauthAuthenticationFilter(ObjectMapper objectMapper) {
        super(new AntPathRequestMatcher("/api/oauth2/token", "POST"));
        this.objectMapper = objectMapper;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {

        OAuthLoginRequest oauthLoginRequest = objectMapper.readValue(request.getInputStream(), OAuthLoginRequest.class);


        return null;
    }
}
