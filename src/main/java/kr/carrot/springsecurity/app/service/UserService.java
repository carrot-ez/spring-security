package kr.carrot.springsecurity.app.service;

import kr.carrot.springsecurity.app.dto.LoginDto;
import kr.carrot.springsecurity.app.dto.response.TokenResponseDto;
import kr.carrot.springsecurity.app.entity.UserEntity;
import kr.carrot.springsecurity.security.exception.PasswordIncorrectException;
import kr.carrot.springsecurity.security.jwt.AuthToken;
import kr.carrot.springsecurity.security.jwt.JwtAuthenticationProvider;
import kr.carrot.springsecurity.security.jwt.Role;
import kr.carrot.springsecurity.app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final JwtAuthenticationProvider jwtAuthTokenProvider;
    private final UserRepository userRepository;

    private final static long ACCESS_TOKEN_VALID_TIME = 1000L * 60 * 30; // 30분
    private final static long REFRESH_TOKEN_VALID_TIME = 1000L * 60 * 60 * 3; // 3시간

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

    @Transactional
    public TokenResponseDto login(LoginDto loginDto) {

        // get username, password
        String username = loginDto.getUsername();
        String password = loginDto.getPassword();

        // find user
        UserEntity userEntity = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("username not found"));

        // check password
        if (!passwordEncoder.matches(password, userEntity.getPassword())) {
            // password 불일치
            throw new PasswordIncorrectException("password incorrect");
        }

        // generate tokens
        String accessToken = createAccessToken(userEntity.getUsername(), userEntity.getRoles()).getToken();
        String refreshToken = createRefreshToken(userEntity.getUsername(), userEntity.getRoles()).getToken();

        return new TokenResponseDto(accessToken, refreshToken);
    }

    private AuthToken createAccessToken(String username, String[] roles) {

        Date expiredDate = new Date(System.currentTimeMillis() + ACCESS_TOKEN_VALID_TIME);
        return jwtAuthTokenProvider.createAuthToken(username, roles, expiredDate);
    }

    public AuthToken createRefreshToken(String username, String[] roles) {

        Date expiredDate = new Date(System.currentTimeMillis() + REFRESH_TOKEN_VALID_TIME);
        return jwtAuthTokenProvider.createAuthToken(username, roles, expiredDate);
    }

}
