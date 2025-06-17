package com.readup.server.common.config;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.readup.server.auth.application.CustomAccessDeniedHandler;
import com.readup.server.auth.application.CustomAuthenticationEntryPoint;
import com.readup.server.auth.application.CustomAuthorizationRequestResolver;
import com.readup.server.auth.application.CustomOAuth2UserService;
import com.readup.server.auth.application.CustomOidcUserService;
import com.readup.server.auth.application.LoginAuthFilter;
import com.readup.server.auth.application.LogoutAuthFilter;
import com.readup.server.auth.application.OAuth2AuthenticationFailureHandler;
import com.readup.server.auth.application.OAuth2AuthenticationSuccessHandler;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

	private final CustomOAuth2UserService customOAuth2UserService;
	private final CustomOidcUserService customOidcUserService;
	private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
	private final OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;
	private final CustomAuthorizationRequestResolver customAuthorizationRequestResolver;
	private final LoginAuthFilter loginAuthFilter;
	private final LogoutAuthFilter logoutAuthFilter;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		return http
			.cors(cors -> cors.configurationSource(corsConfigurationSource()))
			.csrf(AbstractHttpConfigurer::disable)
			.formLogin(AbstractHttpConfigurer::disable)
			.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.NEVER))
			.authorizeHttpRequests(authorize -> authorize
				.requestMatchers("/docs", "/swagger-ui/**", "/api-docs/**", "/springdoc/**").permitAll()
				.requestMatchers("/public/**").permitAll()
				.requestMatchers("/private/**").authenticated()
				.anyRequest().denyAll())
			.oauth2Login(oauth2 -> oauth2
				.authorizationEndpoint(endpoint -> endpoint
					.authorizationRequestResolver(customAuthorizationRequestResolver))
				.redirectionEndpoint(endpoint -> endpoint.baseUri("/api/public/login/oauth2/code/*"))
				.userInfoEndpoint(userInfo -> userInfo
					.userService(customOAuth2UserService)
					.oidcUserService(customOidcUserService)
				)
				.successHandler(oAuth2AuthenticationSuccessHandler)
				.failureHandler(oAuth2AuthenticationFailureHandler))
			.addFilterBefore(loginAuthFilter, UsernamePasswordAuthenticationFilter.class)
			.addFilterBefore(logoutAuthFilter, UsernamePasswordAuthenticationFilter.class)
			.exceptionHandling(exception -> exception
				.authenticationEntryPoint(new CustomAuthenticationEntryPoint())
				.accessDeniedHandler(new CustomAccessDeniedHandler())
			)
			.build();
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(
			Arrays.asList("http://localhost:3000", "http://localhost:3001", "https://read-up.kr", "http://read-up.kr"));
		configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
		configuration.setAllowCredentials(true);
		configuration.setAllowedHeaders(
			Arrays.asList("Authorization", "Content-Type", "X-Requested-With", "Accept", "Origin")
		);
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}
}
