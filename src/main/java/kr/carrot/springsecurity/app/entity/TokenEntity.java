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

    @Builder
    private TokenEntity(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
