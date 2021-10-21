package kr.carrot.springsecurity.security.exception.oauth2;

public class InvalidGrantTypeException extends Oauth2Exception {
    public InvalidGrantTypeException() {
        super("invalid grant type");
    }
}
