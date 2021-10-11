package kr.carrot.springsecurity.security.filter;

import kr.carrot.springsecurity.security.jwt.JwtAuthToken;
import kr.carrot.springsecurity.security.jwt.JwtAuthenticationProvider;
import kr.carrot.springsecurity.security.jwt.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    private JwtAuthenticationProvider jwtAuthTokenProvider;

    public JwtFilter(JwtAuthenticationProvider jwtAuthTokenProvider) {
        this.jwtAuthTokenProvider = jwtAuthTokenProvider;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {

        Optional<String> token = JwtUtils.resolveToken(request, HttpHeaders.AUTHORIZATION);

        if (token.isPresent()) {
            JwtAuthToken jwtAuthToken = jwtAuthTokenProvider.convertAuthToken(token.get());

            if (jwtAuthToken.validate()) {
                Authentication authentication = jwtAuthTokenProvider.getAuthentication(jwtAuthToken);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        chain.doFilter(request, response);
    }
}
