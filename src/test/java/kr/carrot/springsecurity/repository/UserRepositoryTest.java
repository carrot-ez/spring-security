package kr.carrot.springsecurity.repository;

import kr.carrot.springsecurity.entity.UserEntity;
import kr.carrot.springsecurity.security.jwt.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@SpringBootTest
public class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @PersistenceContext
    EntityManager em;


    @Test
    @Transactional
    public void test() {
        UserEntity entity = UserEntity.builder()
                .username("adf")
                .roles(new Role[]{Role.UNKNOWN, Role.USER})
                .build();

        userRepository.save(entity);

        em.flush();

    }

}
