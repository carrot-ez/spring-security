package kr.carrot.springsecurity.security.authentication;

import kr.carrot.springsecurity.app.entity.UserEntity;
import kr.carrot.springsecurity.app.repository.UserRepository;
import kr.carrot.springsecurity.security.jwt.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DefaultUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        UserEntity userEntity = userRepository.findByUsername(username)
                .orElseThrow();

        return User.builder()
                .username(userEntity.getUsername())
                .password(userEntity.getPassword())
                .roles(userEntity.getRoles())
                .build();
    }

    // TODO: DELETE TEST METHOD
    public Long createTestData() {

        String username = "carrot";
        String password = "1234";
        String role = "USER";
        String email = "kian6245@gmail.com";

        String encodedPassword = passwordEncoder.encode(password);

        UserEntity userEntity = UserEntity.builder()
                .username(username)
                .password(encodedPassword)
                .roles(new Role[] {Role.USER, Role.UNKNOWN})
                .email(email)
                .build();

        UserEntity entity = userRepository.save(userEntity);

        return entity.getId();
    }
}
