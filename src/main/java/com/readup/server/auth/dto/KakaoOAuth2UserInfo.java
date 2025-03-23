package com.readup.server.auth.dto;

import java.util.Map;

public class KakaoOAuth2UserInfo extends OAuth2UserInfo {

	public KakaoOAuth2UserInfo(Map<String, Object> attributes) {
		super(attributes);
	}

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
		throw new IllegalArgumentException("KakaoOAuth2UserInfo : No properties found");
	}
}
