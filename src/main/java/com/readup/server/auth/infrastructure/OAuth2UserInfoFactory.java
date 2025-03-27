package com.readup.server.auth.infrastructure;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.readup.server.auth.dto.GoogleOAuth2UserInfo;
import com.readup.server.auth.dto.KakaoOAuth2UserInfo;
import com.readup.server.auth.dto.NaverOAuth2UserInfo;
import com.readup.server.auth.dto.OAuth2UserInfo;
import com.readup.server.auth.exception.UnsupportedOAuthProviderException;

@Component
public class OAuth2UserInfoFactory {

	public OAuth2UserInfo getOAuth2UserInfo(String registrationId, Map<String, Object> attributes) {

		switch (registrationId) {
			case "google":
				return new GoogleOAuth2UserInfo(attributes);
			case "naver":
				return new NaverOAuth2UserInfo(attributes);
			case "kakao":
				return new KakaoOAuth2UserInfo(attributes);
		}
		throw new UnsupportedOAuthProviderException("Unknown registration id : " + registrationId);
	}
}
