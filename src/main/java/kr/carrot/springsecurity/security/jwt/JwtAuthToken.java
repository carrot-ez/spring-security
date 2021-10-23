package kr.carrot.springsecurity.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import kr.carrot.springsecurity.security.utils.HashingUtils;
import lombok.extern.slf4j.Slf4j;

import java.security.Key;
import java.util.Date;
import java.util.Optional;

@Slf4j
public class JwtAuthToken implements AuthToken<Claims> {

    public static final String USERNAME = "username";
    private static final String SALT = "tlas";
    public static final String CLIENT_ID = "CLIENT_ID";
    public static final String SESSION_ID = "SESSION_ID";

    private final String token;
    private final Key key; // java.security.Key


    public JwtAuthToken(String token, Key key) {
        this.token = token;
        this.key = key;
    }

    public JwtAuthToken(String sessionId, Date expiredDate, Key key) {
        this.key = key;
        this.token = generateToken(sessionId, expiredDate).get();
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

    public String getSessionId() {
        return getClaims().get(SESSION_ID, String.class);
    }

    private Optional<String> generateToken(String sessionId, Date expiredDate) {

        Date now = new Date();

        String compact = Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setIssuedAt(now)
                .setExpiration(expiredDate)
                .claim(SESSION_ID, sessionId)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        return Optional.ofNullable(compact);
    }
}
