package com.readup.server.auth.dto;

public class NaverAuthRequest {

    private String grant_type;
    private String client_id;
    private String client_secret;
    private String code;
    private String state;
}
