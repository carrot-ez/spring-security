package kr.carrot.springsecurity.security.jwt;

public interface AuthToken<T> {
    boolean validate();
    T getData();
    String getToken();
}
