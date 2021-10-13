package kr.carrot.springsecurity.app.dto.response;

import lombok.Data;

@Data
public class AuthorizationResponseDto {

    private String code; // authorization code
    private String state;
}
