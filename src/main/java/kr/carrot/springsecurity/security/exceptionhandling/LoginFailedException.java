package kr.carrot.springsecurity.security.exceptionhandling;

public class LoginFailedException extends RuntimeException {

    public LoginFailedException() {
        super(ErrorCode.Login_FAILED.getMessage());
    }

    public LoginFailedException(String message) {
        super(message);
    }
}
