package kr.carrot.springsecurity.security.exceptionhandling;

abstract class UserValidateException extends RuntimeException {

    public UserValidateException(String message) {
        super(message);
    }
}
