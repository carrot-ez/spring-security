package kr.carrot.springsecurity.security.controller;

import kr.carrot.springsecurity.common.CommonResponse;
import kr.carrot.springsecurity.security.dto.LoginDto;
import kr.carrot.springsecurity.security.dto.UserDto;
import kr.carrot.springsecurity.security.exceptionhandling.LoginFailedException;
import kr.carrot.springsecurity.security.jwt.AuthToken;
import kr.carrot.springsecurity.security.jwt.JwtAuthToken;
import kr.carrot.springsecurity.security.service.SecurityUserDetailsService;
import kr.carrot.springsecurity.security.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
public class SecurityContorller {

    private final SecurityUserDetailsService securityUserDetailsService;
    private final UserService userService;

    @GetMapping("/")
    public String index() {
        log.debug("start controller");

        return "spring-security index";
    }

    @GetMapping("/test")
    public String test() {

        Long id = securityUserDetailsService.createTestData();

        return "test-page: " + "create success, id = " + id;
    }

    @PostMapping("/api/v1/login")
    public CommonResponse<?> login(@RequestBody LoginDto loginDto) {

        Optional<UserDto> optionalUserDto = userService.login(loginDto.getUsername(), loginDto.getPassword());

        if (optionalUserDto.isPresent()) {
            JwtAuthToken token = (JwtAuthToken) userService.createToken(optionalUserDto.get());

            return CommonResponse.success(HttpStatus.OK.value(), token.getToken());
        } else {
            throw new LoginFailedException();
        }
    }
}
