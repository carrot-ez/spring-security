package kr.carrot.springsecurity.config;

import kr.carrot.springsecurity.common.CommonResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 로그인 실패
     */
    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    protected CommonResponse<?> handleBadCredentialException(BadCredentialsException e) {
        log.info("handleBadCredentialException", e);

        return CommonResponse.error(HttpStatus.UNAUTHORIZED.value(), e.getMessage());
    }

    /**
     * 토큰 만료시간 경과
     */
    @ExceptionHandler(InsufficientAuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    protected CommonResponse<?> handleInsufficientAuthenticationException(InsufficientAuthenticationException e) {
        log.info("handleInsufficientAuthenticationException", e);

        return CommonResponse.error(HttpStatus.UNAUTHORIZED.value(), e.getMessage());
    }
}
