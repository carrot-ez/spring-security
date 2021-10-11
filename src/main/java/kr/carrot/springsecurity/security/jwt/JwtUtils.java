package kr.carrot.springsecurity.security.jwt;

import org.springframework.http.HttpHeaders;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

public abstract class JwtUtils {

    public static Optional<String> resolveToken(HttpServletRequest request, final String headerName) {

        String token = request.getHeader(headerName);

        if (StringUtils.hasText(token)) {
            return Optional.of(token);
        } else {
            return Optional.empty();
        }
    }
}
