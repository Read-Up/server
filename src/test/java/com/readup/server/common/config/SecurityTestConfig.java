package com.readup.server.common.config;

import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import com.readup.server.auth.application.LoginAuthFilter;
import com.readup.server.auth.application.LogoutAuthFilter;

@TestConfiguration
public class SecurityTestConfig {

	@Bean
	public LoginAuthFilter loginAuthFilter() {
		return Mockito.mock(LoginAuthFilter.class);
	}

	@Bean
	public LogoutAuthFilter logoutAuthFilter() {
		return Mockito.mock(LogoutAuthFilter.class);
	}
}
