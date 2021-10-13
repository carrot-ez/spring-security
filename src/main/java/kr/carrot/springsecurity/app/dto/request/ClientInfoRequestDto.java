package kr.carrot.springsecurity.app.dto.request;

import lombok.Data;

@Data
public class ClientInfoRequestDto {

    private String clientId;
    private String redirectUri;
}
