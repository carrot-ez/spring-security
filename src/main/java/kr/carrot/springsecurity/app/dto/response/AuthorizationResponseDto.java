package kr.carrot.springsecurity.app.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthorizationResponseDto {

    private String code; // authorization code
    private String redirectUri;
}
