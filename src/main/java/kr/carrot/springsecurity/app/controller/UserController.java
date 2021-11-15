package kr.carrot.springsecurity.app.controller;

import kr.carrot.springsecurity.app.dto.LoginDto;
import kr.carrot.springsecurity.app.dto.UserDto;
import kr.carrot.springsecurity.app.dto.common.CommonResponse;
import kr.carrot.springsecurity.app.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.LoginException;
import javax.servlet.http.HttpSession;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/users/v1")
public class UserController {

    private final UserService userService;

    @PostMapping("/login")
    public CommonResponse<UserDto> login(@RequestBody LoginDto loginDto, HttpSession httpSession) throws LoginException {

        UserDto userDto = userService.login(loginDto.getUsername(), loginDto.getPassword());
        httpSession.setAttribute("user", userDto);

        return CommonResponse.success(HttpStatus.OK.value(), userDto);
    }

    @GetMapping("/logout")
    public CommonResponse<Boolean> logout(HttpSession httpSession) {

        httpSession.invalidate();

        return CommonResponse.success(HttpStatus.OK.value(), true);
    }

    @GetMapping("/test")
    public void test(HttpSession httpSession) {

        log.info("user = {}", httpSession.getAttribute("user"));
    }
}
