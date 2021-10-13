package kr.carrot.springsecurity.app.dto.request;

import lombok.Data;

@Data
public class AuthorizationRequestDto {

    private String responseType; // authorization code 방식을 사용시 code
    private String clientId;
    private String redirectUri;
}
