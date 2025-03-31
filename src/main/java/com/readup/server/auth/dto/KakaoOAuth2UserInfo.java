package com.readup.server.auth.dto;

import java.util.Map;

import com.readup.server.auth.exception.OAuth2PropertyNotFoundException;

public record KakaoOAuth2UserInfo(
	Map<String, Object> attributes
) implements OAuth2UserInfo {

	@Override
	public String getId() {
		return attributes.get("id").toString();
	}

	@Override
	public String getName() {

		return (String)getProperties().get("nickname");
	}

	@Override
	public String getEmail() {
		Map<String, Object> kakaoAccount = (Map<String, Object>)attributes.get("kakao_account");
		if (kakaoAccount == null) {
			return null;
		}
		return (String)kakaoAccount.get("email");
	}

	@SuppressWarnings("unchecked")
	private Map<String, Object> getProperties() {
		Object properties = attributes.get("properties");
		if (properties instanceof Map) {
			return (Map<String, Object>)properties;
		}
		throw new OAuth2PropertyNotFoundException("KakaoOAuth2UserInfo : No properties found");
	}
}
