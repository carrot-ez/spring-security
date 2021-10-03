package kr.carrot.springsecurity.config;

import kr.carrot.springsecurity.app.dto.common.CommonResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 로그인 실패
     */
    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public CommonResponse<Object> handleBadCredentialException(BadCredentialsException e) {
        log.error("handleBadCredentialException", e);

        return CommonResponse.error(HttpStatus.UNAUTHORIZED.value(), e.getMessage());
    }

    /**
     * 인증 실패
     */
    @ExceptionHandler(InsufficientAuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public CommonResponse<Object> handleInsufficientAuthenticationException(InsufficientAuthenticationException e) {
        log.error("handleInsufficientAuthenticationException", e);

        return CommonResponse.error(HttpStatus.UNAUTHORIZED.value(), e.getMessage());
    }

    /**
     * 입력 값을 찾을 수 없음
     */
    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CommonResponse<Object> handleNoSuchElementException(NoSuchElementException e) {
        log.error("handleNoSuchElementException", e);

        return CommonResponse.error(HttpStatus.BAD_REQUEST.value(), e.getMessage());
    }
}
