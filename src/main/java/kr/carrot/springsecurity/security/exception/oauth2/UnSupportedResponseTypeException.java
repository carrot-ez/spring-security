package kr.carrot.springsecurity.security.exception.oauth2;

public class UnSupportedResponseTypeException extends Oauth2Exception {

    public UnSupportedResponseTypeException() {
        super("UnSupportedResponseTypeException");
    }
}
