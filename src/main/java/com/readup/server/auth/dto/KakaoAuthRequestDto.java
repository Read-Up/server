package com.readup.server.auth.dto;

import lombok.Getter;

@Getter
public class KakaoAuthRequestDto {

    private String grant_type = "authorization_code";
    private String client_id;
    private String redirect_uri;
    private String code;
}
