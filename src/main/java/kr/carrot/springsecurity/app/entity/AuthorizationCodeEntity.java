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

    private String username;

    private String clientId;

    private String sessionId;

    private LocalDateTime expireDate;

    @Builder
    private AuthorizationCodeEntity(String code, String clientId, LocalDateTime expireDate, String username, String sessionId) {
        this.code = code;
        this.clientId = clientId;
        this.expireDate = expireDate;
        this.username = username;
        this.sessionId = sessionId;
    }

    public boolean isSameClient(String clientId) {
        return this.clientId
                .equals(clientId);
    }

    public boolean isExpired() {
        LocalDateTime now = LocalDateTime.now();

        return expireDate.isBefore(now);
    }
}
