package kr.carrot.springsecurity.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SecurityException;
import kr.carrot.springsecurity.app.dto.UserDto;
import kr.carrot.springsecurity.app.dto.response.TokenResponseDto;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;

import java.security.Key;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;
import java.util.stream.Stream;

@Slf4j
public class JwtAuthToken implements AuthToken<Claims> {

    public static final String USERNAME = "username";

    private final String token;
    private final Key key; // java.security.Key

    private static final String AUTHORITY_ROLE = "role";

    public JwtAuthToken(String token, Key key) {
        this.token = token;
        this.key = key;
    }

    public JwtAuthToken(String username, String[] roles, Date expiredDate, Key key) {
        this.key = key;
        this.token = generateToken(username, roles, expiredDate).get();
    }

    @Override
    public boolean validate() {

        Claims claim = getData();

        return claim != null // not null
                && claim.getExpiration().after(new Date()); // and not expired
    }

    @Override
    public Claims getData() {

        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    @Override
    public String getToken() {
        return this.token;
    }

    public String getUsername(Claims claims) {
        return claims.get(USERNAME, String.class);
    }

    public String[] getRoles(Claims claims) {

        return Arrays.stream(claims.get(AUTHORITY_ROLE, ArrayList.class).toArray())
                .map(Object::toString)
                .toArray(String[]::new);
    }

    private Optional<String> generateToken(String username, String[] roles, Date expiredDate) {

        Date now = new Date();

        String compact = Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setIssuedAt(now)
                .setExpiration(expiredDate)
                .claim(USERNAME, username)
                .claim(AUTHORITY_ROLE, roles)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        return Optional.ofNullable(compact);
    }
}
