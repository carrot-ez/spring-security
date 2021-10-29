package kr.carrot.springsecurity.security.config;

import kr.carrot.springsecurity.security.filter.JwtAccessDeniedHandler;
import kr.carrot.springsecurity.security.filter.JwtAuthenticationEntryPoint;
import kr.carrot.springsecurity.security.jwt.JwtAuthenticationProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String[] PUBLIC_URLS = {
            "/", "/api/oauth/v1/login", "/test", "/api/oauth/v1/token", "/api/client/v1"
    };

    private final JwtAuthenticationProvider jwtAuthTokenProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
    private JwtConfigurer jwtConfigurer() {
        return new JwtConfigurer(jwtAuthTokenProvider);
    }

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
                        "/h2-console/**"
                )
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    /* ==================== CONFIG  ==================== */
}
