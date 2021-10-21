package kr.carrot.springsecurity.app.controller;

import kr.carrot.springsecurity.app.dto.common.CommonResponse;
import kr.carrot.springsecurity.app.dto.response.ClientInfoResponseDto;
import kr.carrot.springsecurity.app.dto.response.TokenResponseDto;
import kr.carrot.springsecurity.app.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/client")
public class ClientController {

    private final ClientService clientService;

    @GetMapping("/callback")
    public CommonResponse<TokenResponseDto> clientCallback(@RequestParam String code, String clientId) {

        ClientInfoResponseDto clientInfoResponseDto = clientService.retrieveClient(clientId);

        // rest call with code
        RestTemplate restTemplate = new RestTemplate();

        URI uri = UriComponentsBuilder.fromUriString("http://localhost:8080/api/v1/token")
                .queryParam("grant_type", "authorization_cde")
                .queryParam("code", code)
                .queryParam("redirect_uri", clientInfoResponseDto.getRedirectUri())
                .queryParam("client_id", clientInfoResponseDto.getClientId())
                .queryParam("client_scret", clientInfoResponseDto.getClientSecret())
                .build().toUri();

        RequestEntity<Object> requestEntity = new RequestEntity<>(HttpMethod.GET, uri);

        ResponseEntity<TokenResponseDto> exchange = restTemplate.exchange(requestEntity, TokenResponseDto.class);
        if (!exchange.hasBody()) {
            throw new RuntimeException("token 발급 실패"); // todo: change exception
        }

        return CommonResponse.success(exchange.getStatusCode().value(), exchange.getBody());
    }
}
