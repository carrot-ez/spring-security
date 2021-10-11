package kr.carrot.springsecurity.app.repository;

import kr.carrot.springsecurity.app.entity.TokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<TokenEntity, Long> {

    Optional<TokenEntity> findByRefreshToken(String refreshToken);
}
