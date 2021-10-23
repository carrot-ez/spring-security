package kr.carrot.springsecurity.app.repository;

import kr.carrot.springsecurity.app.entity.SessionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SessionRepository extends JpaRepository<SessionEntity, Long> {

    Optional<SessionEntity> findBySessionId(String sessionId);
}
