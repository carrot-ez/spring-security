package kr.carrot.springsecurity.security.jwt;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.security.Key;
import java.util.*;

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

    public JwtAuthToken(String username, Collection<? extends GrantedAuthority> authorities, Date expiredDate, Key key) {
        this.key = key;
        this.token = generateToken(username, authorities, expiredDate).get();
    }

    @Override
    public boolean validate() {

        Claims claim = getClaims();

        return claim != null // not null
                && claim.getExpiration().after(new Date()); // and not expired
    }

    @Override
    public Claims getClaims() {

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

    public String getUsername() {
        return getClaims().get(USERNAME, String.class);
    }

    public Collection<? extends GrantedAuthority> getRoles() {

        Claims claims = getClaims();

        return claims.get(AUTHORITY_ROLE, HashSet.class);
    }

    private Optional<String> generateToken(String username, Collection<? extends GrantedAuthority> authorities, Date expiredDate) {

        Date now = new Date();

        String compact = Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setIssuedAt(now)
                .setExpiration(expiredDate)
                .claim(USERNAME, username)
                .claim(AUTHORITY_ROLE, authorities)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        return Optional.ofNullable(compact);
    }
}
