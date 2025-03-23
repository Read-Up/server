package com.readup.server.auth.dto;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.readup.server.auth.entity.SocialAccount;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class CustomOAuth2User implements OAuth2User {

	private Long id;
	@Getter
	private String email;
	private Map<String, Object> attributes;
	private Collection<? extends GrantedAuthority> authorities;

	@Override
	public String getName() {
		return String.valueOf(id);
	}

	@Override
	public Map<String, Object> getAttributes() {
		return attributes;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	public static CustomOAuth2User from(SocialAccount socialAccount, Map<String, Object> attributes) {
		return new CustomOAuth2User(
			socialAccount.getId(),
			socialAccount.getEmail(),
			attributes,
			Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
		);
	}
}
