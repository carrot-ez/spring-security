package kr.carrot.springsecurity.security.jwt;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public interface AuthTokenProvider<T> {
    T createAuthToken(String username, String salt, TokenType tokenType);
    T convertAuthToken(String token);
    Authentication getAuthentication(T authToken);
}
