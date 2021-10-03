package kr.carrot.springsecurity.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SecurityException;
import kr.carrot.springsecurity.app.dto.UserDto;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.security.Key;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;

@Slf4j
public class JwtAuthToken implements AuthToken<Claims> {

    public static final String USERNAME = "username";
    @Getter
    private final String token;
    private final Key key; // java.security.Key

    private static final String AUTHORITY_ROLE = "role";

    public JwtAuthToken(String token, Key key) {
        this.token = token;
        this.key = key;
    }

    public JwtAuthToken(UserDto userDto, Date expiredDate, Key key) {
        this.key = key;
        this.token = generateToken(userDto, expiredDate).get();
    }

    @Override
    public boolean validate() {

        Claims claim = getData();

        return claim != null // not null
                && claim.getExpiration().after(new Date()); // and not expired
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

    public String getUsername(Claims claims) {
        return claims.get(USERNAME, String.class);
    }

    public String[] getRoles(Claims claims) {
        Role[] roles = claims.get(AUTHORITY_ROLE, Role[].class);

        return Arrays.stream(roles)
                .map(Role::getCode)
                .toArray(String[]::new);
    }

    private Optional<String> generateToken(UserDto userDto, Date expiredDate) {

        Date now = new Date();

        String compact = Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setIssuedAt(now)
                .setExpiration(expiredDate)
                .claim(USERNAME, userDto.getUsername())
                .claim(AUTHORITY_ROLE, userDto.getRoles())
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        return Optional.ofNullable(compact);
    }
}
