package kr.carrot.springsecurity.security.exceptionhandling;


public class PasswordIncorrectException extends UserValidateException {

    public PasswordIncorrectException(String message) {
        super(message);
    }
}
