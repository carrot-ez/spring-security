package kr.carrot.springsecurity.security.authentication;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.carrot.springsecurity.app.dto.response.AuthTokenKakao;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class Oauth2KakaoService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    private final String kakaoClientId = "c169d290f662379170b124736e03ed6f";
    private final String kakaoRedirectUri = "http://localhost:3000/callback/kakao";


    public AuthTokenKakao getToken(String code) {

        String grantType = "authorization_code";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // params
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", grantType);
        params.add("client_id", kakaoClientId);
        params.add("redirect_uri", kakaoRedirectUri);
        params.add("code", code);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        String url = "https://kauth.kakao.com/oauth/token";
        try {
            ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
            AuthTokenKakao authentication = objectMapper.readValue(response.getBody(), AuthTokenKakao.class);
            return authentication;
        }
        catch (RestClientException | JsonProcessingException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public String getUserInfo(String accessToken) {

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
        String url = "https://kapi.kakao.com/v2/user/me";

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

            return response.getBody();
        } catch (RestClientException e) {
            throw new RuntimeException(e.getMessage());
        }

    }
}
