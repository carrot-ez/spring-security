package kr.carrot.springsecurity.security.exception.oauth2;

public class InvalidRequestException extends Oauth2Exception {

    public InvalidRequestException() {
        super("InvalidRequestException");
    }
}
