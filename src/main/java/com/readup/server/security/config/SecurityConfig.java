package com.readup.server.security.config;

import com.readup.server.auth.application.CustomOAuthUserService;
import com.readup.server.auth.application.OAuth2AuthenticationFailureHandler;
import com.readup.server.auth.application.OAuth2AuthenticationSuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomOAuthUserService customOAuthUserService;
    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
    private final OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/api/public/**").permitAll()
                        .requestMatchers("/api/private/**").authenticated()
                        .anyRequest().denyAll())
            .oauth2Login(oauth2 -> oauth2
                .authorizationEndpoint(authorize -> authorize.baseUri("/oauth2/authorize"))
                .redirectionEndpoint(redirection -> redirection.baseUri("/oauth2/code/*"))
                .userInfoEndpoint(userInfo -> userInfo.userService(customOAuthUserService))
                .successHandler(oAuth2AuthenticationSuccessHandler)
                .failureHandler(oAuth2AuthenticationFailureHandler))
                .build();
    }
}
