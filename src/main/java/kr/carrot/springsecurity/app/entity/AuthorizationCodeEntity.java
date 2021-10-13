package kr.carrot.springsecurity.app.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AuthorizationCodeEntity {

    @Id
    @GeneratedValue
    private Long id;

    private String code;

    @OneToOne
    @JoinColumn(name = "client_pk")
    private ClientEntity clientId;

    private LocalDateTime expireDate;

    @Builder
    private AuthorizationCodeEntity(String code, ClientEntity clientId, LocalDateTime expireDate) {
        this.code = code;
        this.clientId = clientId;
        this.expireDate = expireDate;
    }
}
