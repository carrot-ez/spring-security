package kr.carrot.springsecurity.app.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ClientInfoResponseDto {

    private String clientId;
    private String clientSecret;
    private String redirectUri;
}
