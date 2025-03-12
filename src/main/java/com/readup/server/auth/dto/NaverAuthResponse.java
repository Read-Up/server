package com.readup.server.auth.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class NaverAuthResponse {

    private String accessToken;
    private String refreshToken;
    private String tokenType;
    private String expiresIn;
    private String error;
    private String errorDescription;
}
