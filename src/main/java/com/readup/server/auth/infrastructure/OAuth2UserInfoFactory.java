package com.readup.server.auth.infrastructure;

import static com.readup.server.common.exception.ErrorCode.*;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.readup.server.auth.dto.GoogleOAuth2UserInfo;
import com.readup.server.auth.dto.KakaoOAuth2UserInfo;
import com.readup.server.auth.dto.NaverOAuth2UserInfo;
import com.readup.server.auth.dto.OAuth2UserInfo;
import com.readup.server.common.exception.ServiceException;

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
		throw new ServiceException(PROVIDER_NOT_FOUND, "Unknown registration id : " + registrationId);
	}
}
