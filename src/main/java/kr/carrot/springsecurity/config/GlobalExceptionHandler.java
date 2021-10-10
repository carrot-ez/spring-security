package kr.carrot.springsecurity.config;

import io.jsonwebtoken.JwtException;
import kr.carrot.springsecurity.app.dto.common.CommonError;
import kr.carrot.springsecurity.app.dto.common.CommonResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /* =============== 400 ERROR [ST] =============== */
    /**
     * 입력 값을 찾을 수 없음
     */
    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CommonResponse<Object> handleNoSuchElementException(NoSuchElementException e) {
        log.error("handleNoSuchElementException", e);

        return CommonResponse.error(HttpStatus.BAD_REQUEST.value(), new CommonError(e));
    }
    /* =============== 400 ERROR [ED] =============== */

    /* =============== 401 ERROR [ST] =============== */
    /**
     * Authentication failed
     */
    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public CommonResponse<Object> handleAuthenticationException(AuthenticationException e) {
        log.error("handleAuthenticationException", e);

        return CommonResponse.error(HttpStatus.BAD_REQUEST.value(), new CommonError(e));
    }

    /**
     * Jwt Token Error
     */
    @ExceptionHandler(JwtException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public CommonResponse<Object> handleJwtException(JwtException e) {
        log.error("handleJwtExceptoin", e);

        return CommonResponse.error(HttpStatus.BAD_REQUEST.value(), new CommonError(e));
    }
    /* =============== 401 ERROR [ED] =============== */

}
