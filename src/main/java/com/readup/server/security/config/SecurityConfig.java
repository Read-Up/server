package com.readup.server.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

import com.readup.server.auth.application.CustomOAuth2UserService;
import com.readup.server.auth.application.OAuth2AuthenticationFailureHandler;
import com.readup.server.auth.application.OAuth2AuthenticationSuccessHandler;
import com.readup.server.auth.application.CustomAuthorizationRequestResolver;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

	private final CustomOAuth2UserService customOAuth2UserService;
	private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
	private final OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;
	private final CustomAuthorizationRequestResolver customAuthorizationRequestResolver;

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
				.authorizationEndpoint(endpoint -> endpoint
					.authorizationRequestResolver(customAuthorizationRequestResolver))
				.redirectionEndpoint(endpoint -> endpoint.baseUri("/oauth2/code/*"))
				.userInfoEndpoint(userInfo -> userInfo.userService(customOAuth2UserService))
				.successHandler(oAuth2AuthenticationSuccessHandler)
				.failureHandler(oAuth2AuthenticationFailureHandler))
			.build();
	}
}
