package kr.carrot.springsecurity.app.dto.request;

import lombok.Data;

@Data
public class AccessTokenRequestDto {

    private String grantType; // authorization_code
    private String code;
    private String redirectUri;
    private String clientId;
}
