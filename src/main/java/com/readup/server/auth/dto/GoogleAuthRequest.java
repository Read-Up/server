package com.readup.server.auth.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class GoogleAuthRequest {

    private String clientId;
    private String clientSecret;
    private String code;
    private String codeVerifier;
    private String grantType = "authorization_code";
    private String redirectUri;
}
