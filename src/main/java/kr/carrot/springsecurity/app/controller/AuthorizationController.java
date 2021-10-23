package kr.carrot.springsecurity.app.controller;

import kr.carrot.springsecurity.annotation.QueryParams;
import kr.carrot.springsecurity.app.dto.LoginDto;
import kr.carrot.springsecurity.app.dto.common.CommonResponse;
import kr.carrot.springsecurity.app.dto.request.TokenRequestDto;
import kr.carrot.springsecurity.app.dto.request.AuthorizationRequestDto;
import kr.carrot.springsecurity.app.dto.request.ClientInfoRequestDto;
import kr.carrot.springsecurity.app.dto.request.RefreshTokenRequestDto;
import kr.carrot.springsecurity.app.dto.response.AuthorizationResponseDto;
import kr.carrot.springsecurity.app.dto.response.ClientInfoResponseDto;
import kr.carrot.springsecurity.app.dto.response.TokenResponseDto;
import kr.carrot.springsecurity.app.service.ClientService;
import kr.carrot.springsecurity.app.service.UserService;
import kr.carrot.springsecurity.security.exception.oauth2.InvalidGrantTypeException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/oauth/v1")
public class AuthorizationController {

    private final UserService userService;
    private final ClientService clientService;

    @GetMapping("/")
    public String index() {
        log.debug("start controller");

        return "spring-security index";
    }

    @GetMapping("/validate")
    public String validate() {
        return "success";
    }

    @PostMapping("/login")
    public CommonResponse<TokenResponseDto> login(@ModelAttribute LoginDto loginDto) {

        log.info("logindto={}", loginDto);

        TokenResponseDto tokens = userService.login(loginDto);

        return CommonResponse.success(HttpStatus.OK.value(), tokens);
    }

    @GetMapping("/auth")
    public ResponseEntity<Void> authorize(@QueryParams AuthorizationRequestDto requestDto) {

        AuthorizationResponseDto auth = clientService.authorize(requestDto);

        URI uri = UriComponentsBuilder.fromUriString(auth.getRedirectUri())
                .queryParam("code", auth.getCode())
                .build()
                .toUri();

        return ResponseEntity.status(HttpStatus.FOUND)
                .location(uri)
                .build();
    }


    @PostMapping("/token")
    public CommonResponse<TokenResponseDto> accessToken(@QueryParams TokenRequestDto requestDto) {

        TokenResponseDto responseDto = null;

        switch (requestDto.getGrantType()) {
            case "authorization_code":

                break;
            case "refresh_token":
                responseDto = userService.refreshingToken(requestDto);
                break;
            default:
                throw new InvalidGrantTypeException();
        }
    }
}
