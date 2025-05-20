package com.readup.server.auth.application;

import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;

@Component
public class CustomAuthorizationRequestResolver implements OAuth2AuthorizationRequestResolver {

	private static final String AUTHORIZATION_REQUEST_BASE_URI = "/api/public/oauth2";
	private static final String QUERY_PARAM = "redirect";
	private final OAuth2AuthorizationRequestResolver defaultResolver;

	public CustomAuthorizationRequestResolver(ClientRegistrationRepository clientRegistrationRepository) {
		this.defaultResolver = new DefaultOAuth2AuthorizationRequestResolver(clientRegistrationRepository,
			AUTHORIZATION_REQUEST_BASE_URI);
	}

	@Override
	public OAuth2AuthorizationRequest resolve(HttpServletRequest request) {
		OAuth2AuthorizationRequest authorizationRequest = defaultResolver.resolve(request);

		return customizeAuthorizationRequest(request, authorizationRequest);
	}

	@Override
	public OAuth2AuthorizationRequest resolve(HttpServletRequest request, String clientRegistrationId) {
		OAuth2AuthorizationRequest authorizationRequest = defaultResolver.resolve(request, clientRegistrationId);

		return customizeAuthorizationRequest(request, authorizationRequest);
	}

	private OAuth2AuthorizationRequest customizeAuthorizationRequest(HttpServletRequest request,
		OAuth2AuthorizationRequest authorizationRequest) {

		String redirectUri = String.valueOf(request.getParameter(QUERY_PARAM));

		if (authorizationRequest == null) {
			return null;
		}

		return OAuth2AuthorizationRequest.from(authorizationRequest)
			.state(authorizationRequest.getState() + "?" + redirectUri)
			.build();
	}
}
