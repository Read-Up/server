package com.readup.server.auth.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class NaverAuthRequest {

    private String grantType;
    private String clientId;
    private String clientSecret;
    private String code;
    private String state;
}
