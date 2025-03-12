package com.readup.server.auth.dto;

public class GoogleAuthRequest {

    private String client_id;
    private String client_secret;
    private String code;
    private String code_verifier;
    private String grant_type = "authorization_code";
    private String redirect_uri;
}
