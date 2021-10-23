package kr.carrot.springsecurity.app.dto;

import lombok.Data;

@Data
public class LoginDto {

    private String username;
    private String password;
    private String clientId;
    private String code; // use grantType = authorization_code
}
