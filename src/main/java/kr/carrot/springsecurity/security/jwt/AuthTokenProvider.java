package kr.carrot.springsecurity.security.jwt;

import kr.carrot.springsecurity.app.dto.UserDto;
import org.springframework.security.core.Authentication;

import java.util.Date;

public interface AuthTokenProvider<T> {
    T createAuthToken(String username, String[] roles, Date expiredDate);
    T convertAuthToken(String token);
    Authentication getAuthentication(T authToken);
}
