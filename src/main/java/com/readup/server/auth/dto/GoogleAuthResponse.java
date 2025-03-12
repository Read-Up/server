package com.readup.server.auth.dto;

import lombok.Getter;

@Getter
public class GoogleAuthResponse {

    private String access_token;
    private Integer expires_in;
    private String refresh_token;
    private String scope;
    private String token_type;
}
