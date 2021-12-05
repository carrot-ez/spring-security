package kr.carrot.springsecurity.config;

import kr.carrot.springsecurity.app.entity.ClientEntity;
import kr.carrot.springsecurity.app.entity.UserEntity;
import kr.carrot.springsecurity.app.repository.ClientRepository;
import kr.carrot.springsecurity.app.repository.UserRepository;
import kr.carrot.springsecurity.security.jwt.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.UUID;

@Configuration
@RequiredArgsConstructor
public class AppConfig {

    private final UserRepository userRepository;
    private final ClientRepository clientRepository;
    private final PasswordEncoder passwordEncoder;

    // todo: delete test method
    @PostConstruct
    @Transactional
    public void initTestData() {

        // create user
        String username = "carrot";
        String password = "1234";
        String email = "kian6245@gmail.com";

        String encodedPassword = passwordEncoder.encode(password);

        UserEntity userEntity = UserEntity.builder()
                .username(username)
                .password(encodedPassword)
                .authorities(new Role[] {Role.USER, Role.UNKNOWN})
                .email(email)
                .build();

        userRepository.save(userEntity);

        // create client
        ClientEntity clientEntity = ClientEntity.builder()
                .clientId("123")
                .clientSecret(UUID.randomUUID().toString())
                .redirectUri("http://localhost:8080/")
                .build();

        clientRepository.save(clientEntity);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
