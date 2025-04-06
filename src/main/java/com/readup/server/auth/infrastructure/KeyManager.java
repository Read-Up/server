package com.readup.server.auth.infrastructure;

import java.nio.charset.StandardCharsets;
import java.security.Key;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "spring.security.oauth2.keys")
public class KeyManager {

	private String accessTokenKeyString;
	private String refreshTokenKeyString;

	private Key accessTokenKey;
	private Key refreshTokenKey;

	@PostConstruct
	public void init() {
		this.accessTokenKey = Keys.hmacShaKeyFor(accessTokenKeyString.getBytes(StandardCharsets.UTF_8));
		this.refreshTokenKey = Keys.hmacShaKeyFor(refreshTokenKeyString.getBytes(StandardCharsets.UTF_8));
	}
}
