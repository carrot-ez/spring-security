package kr.carrot.springsecurity.app.controller;

import kr.carrot.springsecurity.app.dto.common.CommonResponse;
import kr.carrot.springsecurity.app.dto.LoginDto;
import kr.carrot.springsecurity.app.dto.response.TokenResponseDto;
import kr.carrot.springsecurity.app.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {

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

    @PostMapping("/api/v1/login")
    public CommonResponse<?> login(@RequestBody LoginDto loginDto) {

        TokenResponseDto tokens = userService.login(loginDto);
        
        return CommonResponse.success(HttpStatus.OK.value(), tokens);
    }

    @GetMapping("/validate")
    public String validate() {
        return "success";
    }
}
