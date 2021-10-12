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

    private final String token;
    private final Key key; // java.security.Key


    public JwtAuthToken(String token, Key key) {
        this.token = token;
        this.key = key;
    }

    public JwtAuthToken(String username, String salt, Date expiredDate, Key key) {
        this.key = key;
        this.token = generateToken(username, salt, expiredDate).get();
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

    public String getEncryptedSalt() {
        return getClaims().get(SALT, String.class);
    }

    private Optional<String> generateToken(String username, String salt, Date expiredDate) {

        Date now = new Date();

        String compact = Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setIssuedAt(now)
                .setExpiration(expiredDate)
                .claim(USERNAME, username)
                .claim(SALT, HashingUtils.encryptSha256(salt))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        return Optional.ofNullable(compact);
    }
}
