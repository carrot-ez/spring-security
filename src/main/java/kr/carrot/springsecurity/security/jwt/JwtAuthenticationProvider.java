package kr.carrot.springsecurity.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import kr.carrot.springsecurity.security.exception.InvalidJwtTokenException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.security.Key;
import java.util.Date;

@Slf4j
public class JwtAuthenticationProvider implements AuthTokenProvider<JwtAuthToken> {

    private static final String AUTHORITY_ROLE = "role";

    private final Key key;
    private final UserDetailsService userDetailsService;

    public JwtAuthenticationProvider(String base64Secret, UserDetailsService userDetailsService) {
        byte[] keyBytes = Decoders.BASE64.decode(base64Secret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.userDetailsService = userDetailsService;
    }

    @Override
    public JwtAuthToken createAuthToken(String username, String[] roles, Date expiredDate) {
        return new JwtAuthToken(username, roles, expiredDate, key);
    }

    @Override
    public JwtAuthToken convertAuthToken(String token) {
        return new JwtAuthToken(token, key);
    }

    @Override
    public Authentication getAuthentication(JwtAuthToken authToken) {

        if(!authToken.validate()) {
            throw new InvalidJwtTokenException();
        }

        // get user details
        Claims claims = authToken.getData();
        String username = authToken.getUsername(claims);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        // set authentication true
        // authentication manager 또는 authentication provider에서 충분한 인증/인가가 이루어졌을때만 사용해야 하는 생성자.
        // method desc 참고
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }
}
