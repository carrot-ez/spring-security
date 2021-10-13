package kr.carrot.springsecurity.app.repository;

import kr.carrot.springsecurity.app.entity.AuthorizationCodeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthorizationCodeRepository extends JpaRepository<AuthorizationCodeEntity, Long> {

    Optional<AuthorizationCodeEntity> findByCode(String code);
}
