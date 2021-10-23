package kr.carrot.springsecurity.app.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.tomcat.jni.Local;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

// TODO: CHANGE REDIS
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SessionEntity {

    @Id @GeneratedValue
    private Long id;

    private String sessionId;

    private String username;
    private String clientId;
    private LocalDateTime expireDt;

    @Builder
    private SessionEntity(String sessionId, String username, String clientId) {
        this.sessionId = sessionId;
        this.username = username;
        this.clientId = clientId;
        expireDt = LocalDateTime.now().plusMinutes(30L); // todo: change constant
    }

    public boolean isExpired() {
        return expireDt.isBefore(LocalDateTime.now());
    }
}
