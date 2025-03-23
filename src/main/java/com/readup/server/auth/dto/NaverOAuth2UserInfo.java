package com.readup.server.auth.dto;

import java.util.Map;

public class NaverOAuth2UserInfo extends OAuth2UserInfo {

	public NaverOAuth2UserInfo(Map<String, Object> attributes) {
		super(attributes);
	}

	@Override
	public String getId() {

		return (String)getResponse().get("id");
	}

	@Override
	public String getName() {

		return (String)getResponse().get("name");
	}

	@Override
	public String getEmail() {

		return (String)getResponse().get("email");
	}

	@SuppressWarnings("unchecked")
	private Map<String, Object> getResponse() {
		Object response = attributes.get("response");
		if (response instanceof Map) {
			return (Map<String, Object>)response;
		}
		throw new IllegalArgumentException("NaverOAuth2UserInfo : No response found");
	}
}
