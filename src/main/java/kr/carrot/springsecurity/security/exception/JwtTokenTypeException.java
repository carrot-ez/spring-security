package kr.carrot.springsecurity.security.exception;

import io.jsonwebtoken.JwtException;

public class JwtTokenTypeException extends JwtException {

    public JwtTokenTypeException() {
        super("Invalid jwt token type error");
    }
}
