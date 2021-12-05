package kr.carrot.springsecurity.app.controller;

import kr.carrot.springsecurity.annotation.QueryParams;
import kr.carrot.springsecurity.app.constant.Constants;
import kr.carrot.springsecurity.app.dto.LoginDto;
import kr.carrot.springsecurity.app.dto.common.CommonResponse;
import kr.carrot.springsecurity.app.dto.request.AuthorizationRequestDto;
import kr.carrot.springsecurity.app.dto.request.TokenRequestDto;
import kr.carrot.springsecurity.app.dto.response.AuthTokenKakao;
import kr.carrot.springsecurity.app.dto.response.TokenResponseDto;
import kr.carrot.springsecurity.app.service.ClientService;
import kr.carrot.springsecurity.app.service.OauthService;
import kr.carrot.springsecurity.security.authentication.Oauth2KakaoService;
import kr.carrot.springsecurity.security.exception.oauth2.InvalidGrantTypeException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/oauth/v1")
public class OauthController {

    private final OauthService oauthService;
    private final ClientService clientService;
    private final Oauth2KakaoService oauth2KakaoService;

    @GetMapping("/")
    public String index() {
        log.debug("start controller");

        return "spring-security index";
    }

    @GetMapping("/validate")
    public String validate() {
        return "success";
    }

    /**
     * client 접근 권한 확인
     *
     * @param requestDto
     * @return
     */
    @GetMapping("/auth")
    public CommonResponse<Boolean> authorize(@QueryParams AuthorizationRequestDto requestDto) {

        // check request info
        clientService.authorize(requestDto);

        return CommonResponse.success(HttpStatus.OK.value(), true);
    }

    /**
     * 로그인 API
     * 로그인 후 ssession & authorization code 생성
     *
     * @param loginDto
     * @return authorization code
     */
    @PostMapping("/login")
    public CommonResponse<String> login(@Valid @ModelAttribute LoginDto loginDto) {

        log.info("logindto={}", loginDto);

        String authorizationCode = oauthService.login(loginDto);

        return CommonResponse.success(HttpStatus.OK.value(), authorizationCode);
    }


    /**
     * token 발행 API
     *
     * @param requestDto
     * @return
     */
    @GetMapping("/token")
    public CommonResponse<TokenResponseDto> accessToken(@QueryParams TokenRequestDto requestDto) {

        TokenResponseDto responseDto = null;

        switch (requestDto.getGrantType()) {
            case Constants.GRANT_TYPE_AUTHORIZATION_CODE:
                responseDto = oauthService.accessToken(requestDto);
                break;
            case Constants.GRANT_TYPE_REFRESH_TOKEN:
                responseDto = oauthService.refreshToken(requestDto);
                break;
            default:
                throw new InvalidGrantTypeException();
        }

        return CommonResponse.success(HttpStatus.OK.value(), responseDto);
    }

    @GetMapping("/kakao")
    public String kakaoLogin(String code) {
        AuthTokenKakao authTokenKakao = oauth2KakaoService.callTokenApi(code);
        log.info("kakao auth token = {}", authTokenKakao);

        String userInfo = oauth2KakaoService.getUserInfo(authTokenKakao.getAccess_token());
        log.info("kakao user info = {}", userInfo);

        return userInfo;
    }
}
