package kr.carrot.springsecurity.app.controller;

import kr.carrot.springsecurity.app.dto.common.CommonResponse;
import kr.carrot.springsecurity.app.dto.LoginDto;
import kr.carrot.springsecurity.app.dto.response.TokenResponseDto;
import kr.carrot.springsecurity.app.service.UserService;
import kr.carrot.springsecurity.security.jwt.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {

    public static final String X_REFRESH_TOKEN = "X-Refresh-Token";

    private final UserService userService;

    @GetMapping("/")
    public String index() {
        log.debug("start controller");

        return "spring-security index";
    }

    @GetMapping("/test")
    public String test() {

        log.info("test");

        Long id = userService.createTestData();

        return "test-page: " + "create success, id = " + id;
    }

    @GetMapping("/validate")
    public String validate() {
        return "success";
    }


    @PostMapping("/api/v1/login")
    public CommonResponse<TokenResponseDto> login(@RequestBody LoginDto loginDto) {

        TokenResponseDto tokens = userService.login(loginDto);
        
        return CommonResponse.success(HttpStatus.OK.value(), tokens);
    }

    @PostMapping("/api/v1/token")
    public CommonResponse<TokenResponseDto> refreshingToken(HttpServletRequest request) {

        Optional<String> token = JwtUtils.resolveToken(request, X_REFRESH_TOKEN);
        TokenResponseDto tokenResponseDto = userService.refreshingToken(token);

        return CommonResponse.success(HttpStatus.OK.value(), tokenResponseDto);
    }
}
