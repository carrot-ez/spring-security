package kr.carrot.springsecurity.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import kr.carrot.springsecurity.app.dto.UserDto;
import kr.carrot.springsecurity.security.exceptionhandling.TokenValidFailedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.Key;
import java.util.Date;

@Slf4j
public class JwtAuthTokenProvider implements AuthTokenProvider<JwtAuthToken> {

    private final Key key;
    private static final String AUTHORITY_ROLE = "role";

    public JwtAuthTokenProvider(String base64Secret) {
        byte[] keyBytes = Decoders.BASE64.decode(base64Secret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    @Override
    public JwtAuthToken createAuthToken(UserDto userDto, Date expiredDate) {
        return new JwtAuthToken(userDto, expiredDate, key);
    }

    @Override
    public JwtAuthToken convertAuthToken(String token) {
        return new JwtAuthToken(token, key);
    }

    @Override
    public Authentication getAuthentication(JwtAuthToken authToken) {

        if(!authToken.validate()) {
            throw new TokenValidFailedException();
        }

        // get claims
        Claims claims = authToken.getData();

        // build UserDetails
        UserDetails principal = User.builder()
                .username(authToken.getUsername(claims))
                .roles(authToken.getRoles(claims))
                .build();

        return new UsernamePasswordAuthenticationToken(principal, authToken);
    }
}
