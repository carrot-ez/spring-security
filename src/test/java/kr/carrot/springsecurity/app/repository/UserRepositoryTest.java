package kr.carrot.springsecurity.app.repository;

import kr.carrot.springsecurity.app.entity.UserEntity;
import kr.carrot.springsecurity.security.jwt.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Date;

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
                .authorities(new Role[]{Role.UNKNOWN, Role.USER})
                .build();

        userRepository.save(entity);

        em.flush();

    }

    @Test
    public void test_2() {
        long ext = 1000L * 60 * 30;
        Date date = new Date(System.currentTimeMillis() + ext);

        System.out.println("date = " + date);
    }

}
