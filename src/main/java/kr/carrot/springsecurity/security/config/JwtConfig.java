package kr.carrot.springsecurity.security.config;

import kr.carrot.springsecurity.security.authentication.DefaultUserDetailsService;
import kr.carrot.springsecurity.security.jwt.JwtAuthenticationProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class JwtConfig {

    @Value("${jwt.base64-secret}")
    private String base64Secret;

    private final DefaultUserDetailsService defaultUserDetailsService;

    @Bean
    public JwtAuthenticationProvider jwtProvider() {
        return new JwtAuthenticationProvider(base64Secret, defaultUserDetailsService);
    }
}
