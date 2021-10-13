package kr.carrot.springsecurity.app.dto.request;

import lombok.Data;

@Data
public class AuthorizationRequestDto {

    private String responseType;
    private String clientId;
    private String redirectUrl;
    private String scope;
    private String state;
}
