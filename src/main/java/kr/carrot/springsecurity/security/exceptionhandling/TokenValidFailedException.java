package kr.carrot.springsecurity.security.exceptionhandling;

public class TokenValidFailedException extends RuntimeException {

    public TokenValidFailedException() {
        super(ErrorCode.TOKEN_GENERATION_FAILED.getMessage());
    }

    public TokenValidFailedException(String message) {
        super(message);
    }
}
