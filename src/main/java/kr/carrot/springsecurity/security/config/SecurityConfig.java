package kr.carrot.springsecurity.security.config;

import kr.carrot.springsecurity.security.service.SecurityUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final SecurityUserDetailsService securityUserDetailsService;

    /* ==================== CONFIG  ==================== */
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        // authorize request
        http.authorizeRequests()
                .antMatchers("/", "/test", "/h2-console/**")
                .permitAll()
                .antMatchers("/**")
                .authenticated();

        http.csrf()
                .ignoringAntMatchers("/h2-console/**");

        http.headers()
                .frameOptions() // X-Frame-Options header
                    .sameOrigin();

        http.formLogin();

        http.httpBasic();
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        // set custom UserDetailsService
        auth.userDetailsService(securityUserDetailsService);
    }

    /* ==================== CONFIG  ==================== */
}
