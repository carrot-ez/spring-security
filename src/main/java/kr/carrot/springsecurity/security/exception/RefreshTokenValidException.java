package kr.carrot.springsecurity.security.exception;

import io.jsonwebtoken.JwtException;

public class RefreshTokenValidException extends JwtException {

    public RefreshTokenValidException(String message) {
        super("[refresh token invalid] " + message);
    }
}
