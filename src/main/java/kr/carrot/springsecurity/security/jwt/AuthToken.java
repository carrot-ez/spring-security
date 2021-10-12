package kr.carrot.springsecurity.security.jwt;

public interface AuthToken<T> {
    boolean validate();
    T getClaims();
    String getToken();
}
