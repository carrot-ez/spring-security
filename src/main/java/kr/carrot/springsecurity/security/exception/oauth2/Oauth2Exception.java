package kr.carrot.springsecurity.security.exception.oauth2;

public abstract class Oauth2Exception extends RuntimeException {

    public Oauth2Exception(String message) {
        super("[Oauth2 Exception] " + message);
    }
}
