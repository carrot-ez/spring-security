package kr.carrot.springsecurity.security.config;

import kr.carrot.springsecurity.security.exceptionhandling.JwtAccessDeniedHandler;
import kr.carrot.springsecurity.security.exceptionhandling.JwtAuthenticationEntryPoint;
import kr.carrot.springsecurity.security.jwt.JwtAuthTokenProvider;
import kr.carrot.springsecurity.security.service.SecurityUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final SecurityUserDetailsService securityUserDetailsService;
    private final JwtAuthTokenProvider jwtAuthTokenProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    /* ==================== CONFIG  ==================== */
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        // not used in RestAPI
        http.csrf().disable();
//        http.formLogin().disable();

        http.exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(jwtAccessDeniedHandler);

        // h2-console 사용을 위한 설정
        http.headers()
                .frameOptions() // X-Frame-Options header
                    .sameOrigin();

//        http.cors();

        // spring security don't have to manage session
        http.sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        // authorize request
        http.authorizeRequests()
                .antMatchers("/api/v1/login", "/test")
                .permitAll()
                .anyRequest()
                .authenticated();

        http.apply(jwtConfigurer());

    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .antMatchers(
                        "/",
                        "/h2-console/**",
                        "/favicon.ico"
                );
    }

    /* ==================== CONFIG  ==================== */

    private JwtConfigurer jwtConfigurer() {
        return new JwtConfigurer(jwtAuthTokenProvider);
    }
}
