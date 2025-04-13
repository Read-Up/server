package com.readup.server.auth.dto;

import static com.readup.server.common.exception.ErrorCode.*;

import java.util.Map;

import com.readup.server.common.exception.ServiceException;

public record NaverOAuth2UserInfo(
	Map<String, Object> attributes
) implements OAuth2UserInfo {

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
		throw new ServiceException(PROPERTY_NOT_FOUND, "NaverOAuth2UserInfo : No response found");
	}
}
