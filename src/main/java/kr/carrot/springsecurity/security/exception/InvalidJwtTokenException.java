package kr.carrot.springsecurity.security.exception;

import io.jsonwebtoken.JwtException;

public class InvalidJwtTokenException extends JwtException {

    private static final String DEFAULT_MESSAGE = "Invalid Jwt Token Error ";

    public InvalidJwtTokenException() {
        super(DEFAULT_MESSAGE);
    }

    public InvalidJwtTokenException(String message) {
        super(DEFAULT_MESSAGE + message);
    }
}
