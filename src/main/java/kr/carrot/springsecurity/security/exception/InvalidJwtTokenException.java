package kr.carrot.springsecurity.security.exception;

import io.jsonwebtoken.JwtException;

public class InvalidJwtTokenException extends JwtException {

    public InvalidJwtTokenException() {
        super("Invalid Jwt Token Error");
    }
}
