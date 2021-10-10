package kr.carrot.springsecurity.security.exception;


import org.springframework.security.core.AuthenticationException;

public class PasswordIncorrectException extends AuthenticationException {

    public PasswordIncorrectException(String message) {
        super(message);
    }
}
