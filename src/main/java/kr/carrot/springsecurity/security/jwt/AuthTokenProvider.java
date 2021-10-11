package kr.carrot.springsecurity.security.jwt;

import org.springframework.security.core.Authentication;

public interface AuthTokenProvider<T> {
    T createAuthToken(String username, String[] roles, TokenType tokenType);
    T convertAuthToken(String token);
    Authentication getAuthentication(T authToken);
}
