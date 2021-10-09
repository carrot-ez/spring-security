package kr.carrot.springsecurity.security.config;

import kr.carrot.springsecurity.security.filter.JwtFilter;
import kr.carrot.springsecurity.security.jwt.JwtAuthenticationProvider;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class JwtConfigurer extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    private JwtAuthenticationProvider jwtAuthTokenProvider;

    public JwtConfigurer(JwtAuthenticationProvider jwtAuthTokenProvider) {
        this.jwtAuthTokenProvider = jwtAuthTokenProvider;
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        JwtFilter jwtFilter = new JwtFilter(jwtAuthTokenProvider);

        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
