package kr.carrot.springsecurity.security.controller;

import kr.carrot.springsecurity.security.service.SecurityUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SecurityContorller {

    private final SecurityUserDetailsService securityUserDetailsService;

    @GetMapping("/")
    public String index() {

        return "spring-security index";
    }

    @GetMapping("/test")
    public String test() {

        Long id = securityUserDetailsService.createTestData();

        return "test-page: " + "create success, id = " + id;
    }
}
