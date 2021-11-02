package kr.carrot.springsecurity.app.controller;

import kr.carrot.springsecurity.annotation.QueryParams;
import kr.carrot.springsecurity.app.constant.Constant;
import kr.carrot.springsecurity.app.dto.LoginDto;
import kr.carrot.springsecurity.app.dto.common.CommonResponse;
import kr.carrot.springsecurity.app.dto.request.AuthorizationRequestDto;
import kr.carrot.springsecurity.app.dto.request.TokenRequestDto;
import kr.carrot.springsecurity.app.dto.response.TokenResponseDto;
import kr.carrot.springsecurity.app.service.ClientService;
import kr.carrot.springsecurity.app.service.UserService;
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

    /**
     * client 접근 권한 확인
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
     * @param loginDto
     * @return authorization code
     */
    @PostMapping("/login")
    public CommonResponse<String> login(@Valid @ModelAttribute LoginDto loginDto) {

        log.info("logindto={}", loginDto);

        String authorizationCode = userService.login(loginDto);

        return CommonResponse.success(HttpStatus.OK.value(), authorizationCode);
    }



    /**
     * token 발행 API
     * @param requestDto
     * @return
     */
    @PostMapping("/token")
    public CommonResponse<TokenResponseDto> accessToken(@QueryParams TokenRequestDto requestDto) {

        TokenResponseDto responseDto = null;

        switch (requestDto.getGrantType()) {
            case Constant.GRANT_TYPE_AUTHORIZATION_CODE:
                responseDto = userService.accessToken(requestDto);
                break;
            case Constant.GRANT_TYPE_REFRESH_TOKEN:
                responseDto = userService.refreshToken(requestDto);
                break;
            default:
                throw new InvalidGrantTypeException();
        }

        return CommonResponse.success(HttpStatus.OK.value(), responseDto);
    }
}
