package kr.carrot.springsecurity.app.service;

import kr.carrot.springsecurity.app.dto.UserDto;
import kr.carrot.springsecurity.app.entity.UserEntity;
import kr.carrot.springsecurity.app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.security.auth.login.LoginException;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserDto login(String username, String password) throws LoginException {

        UserEntity userEntity = userRepository.findByUsername(username)
                .orElseThrow();

        if (!passwordEncoder.matches(password, userEntity.getPassword())) {
            throw new LoginException("password incorrect");
        }

        return new UserDto(username, userEntity.getEmail(), userEntity.getAuthorities());
    }
}
