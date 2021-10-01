package kr.carrot.springsecurity.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SecurityException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.net.http.HttpHeaders;
import java.security.Key;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

@Slf4j
public class JwtAuthToken implements AuthToken<Claims> {

    @Getter
    private final String token;
    private final Key key; // java.security.Key

    private static final String AUTORITIES_KEY = "role";

    public JwtAuthToken(String token, Key key) {
        this.token = token;
        this.key = key;
    }

    public JwtAuthToken(String id, String role, Date expiredDate, Key key) {
        this.key = key;
        this.token = createJwtAuthToken(id, role, expiredDate).get();
    }

    @Override
    public boolean validate() {
        return getData() != null;
    }

    @Override
    public Claims getData() {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

        }catch (SecurityException e) {
            log.info("Invalid JWT signature.");
        } catch (MalformedJwtException e) {
            log.info("Invalid JWT token.");
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT token.");
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT token.");
        } catch (IllegalArgumentException e) {
            log.info("JWT token compact of handler are invalid.");
        }
        return null;
    }

    private Optional<String> createJwtAuthToken(String id, String role, Date expiredDate) {

        Date now = new Date();

        String compact = Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setIssuer("Carrot")
                .setSubject(id)
                .setIssuedAt(now)
                .setExpiration(expiredDate)
                .claim(AUTORITIES_KEY, role)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        return Optional.ofNullable(compact);
    }
}
