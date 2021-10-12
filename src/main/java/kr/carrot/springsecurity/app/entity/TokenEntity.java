package kr.carrot.springsecurity.app.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TokenEntity {

    @Id
    @GeneratedValue
    private Long id;

    private String refreshToken;
    private String salt;

    @Builder
    private TokenEntity(String refreshToken, String salt) {
        this.refreshToken = refreshToken;
        this.salt = salt;
    }
}
