package com.readup.server.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class CreateSocialAccountRequest {

	private String email;
	private String provider;
	private String providerUid;

	public static CreateSocialAccountRequest of(String email, String provider, String providerUid) {
		return CreateSocialAccountRequest.builder()
			.email(email)
			.provider(provider)
			.providerUid(providerUid)
			.build();
	}
}
