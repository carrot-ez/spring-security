package kr.carrot.springsecurity.security.config;

import kr.carrot.springsecurity.security.authentication.DefaultUserDetailsService;
import kr.carrot.springsecurity.security.exceptionhandling.JwtAccessDeniedHandler;
import kr.carrot.springsecurity.security.exceptionhandling.JwtAuthenticationEntryPoint;
import kr.carrot.springsecurity.security.jwt.JwtAuthTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String[] PUBLIC_URLS = {
            "/", "/api/v1/login", "/test"
    };

    private final DefaultUserDetailsService defaultUserDetailsService;
    private final JwtAuthTokenProvider jwtAuthTokenProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    /* ==================== CONFIG  ==================== */
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        // not used in RestAPI
        http.csrf().disable()
                .formLogin().disable()
                .logout().disable()
                .httpBasic().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS); // spring security don't have to manage session

        http.cors();

        http.exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint) // 인증 예외 처리
                .accessDeniedHandler(jwtAccessDeniedHandler); // 인가 예외 처리

        // h2-console 사용을 위한 설정
        http.headers()
                .frameOptions() // X-Frame-Options header
                    .sameOrigin();

        // authorize request
        http.authorizeRequests()
                .antMatchers(PUBLIC_URLS).permitAll()
                .anyRequest().authenticated();

//        // add jwt filter
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
