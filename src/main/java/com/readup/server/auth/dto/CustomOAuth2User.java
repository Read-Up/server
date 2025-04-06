package com.readup.server.auth.dto;

import static com.readup.server.auth.dto.UserRole.*;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.readup.server.auth.domain.SocialAccount;

public record CustomOAuth2User(
	Long id,
	String email,
	Map<String, Object> attributes,
	List<GrantedAuthority> authorities
) implements OAuth2User {

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
			List.of(new SimpleGrantedAuthority(ROLE_USER.name()))
		);
	}
}
