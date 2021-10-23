package kr.carrot.springsecurity.app.dto.request;

import lombok.Data;

@Data
public class TokenRequestDto {

    private String grantType; // authorization_code
    private String code;
    private String redirectUri;
    private String clientId;
    private String clientSecret;

    private String refreshToken; // use grantType = refresh_token
}
