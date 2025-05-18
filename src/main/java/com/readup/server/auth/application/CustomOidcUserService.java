package com.readup.server.auth.application;

import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.readup.server.auth.domain.SocialAccount;
import com.readup.server.auth.domain.SocialAccountService;
import com.readup.server.auth.dto.CreateSocialAccountRequest;
import com.readup.server.auth.dto.CustomOAuth2User;
import com.readup.server.auth.dto.OAuth2UserInfo;
import com.readup.server.auth.infrastructure.OAuth2UserInfoFactory;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomOidcUserService extends OidcUserService {

	private final SocialAccountService socialAccountService;
	private final OAuth2UserInfoFactory oAuth2UserInfoFactory;

	@Override
	public OidcUser loadUser(OidcUserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
		OidcUser oAuth2User = loadUserFromSuper(oAuth2UserRequest);

		return createOAuth2User(oAuth2UserRequest, oAuth2User);
	}

	protected OidcUser loadUserFromSuper(OidcUserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
		return super.loadUser(oAuth2UserRequest);
	}

	private OidcUser createOAuth2User(OAuth2UserRequest oAuth2UserRequest, OAuth2User oAuth2User) {
		String registrationId = oAuth2UserRequest.getClientRegistration().getRegistrationId();
		OAuth2UserInfo oAuth2UserInfo = oAuth2UserInfoFactory.getOAuth2UserInfo(registrationId,
			oAuth2User.getAttributes());

		SocialAccount socialAccount = socialAccountService.saveOrGet(
			CreateSocialAccountRequest.of(oAuth2UserInfo.getEmail(), registrationId, oAuth2UserInfo.getId()));

		return CustomOAuth2User.from(socialAccount, oAuth2User.getAttributes());
	}
}
