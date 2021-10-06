package kr.carrot.springsecurity.app.service;

import kr.carrot.springsecurity.app.dto.UserDto;
import kr.carrot.springsecurity.app.dto.response.TokenResponseDto;
import kr.carrot.springsecurity.app.entity.UserEntity;
import kr.carrot.springsecurity.security.exceptionhandling.PasswordIncorrectException;
import kr.carrot.springsecurity.security.jwt.AuthToken;
import kr.carrot.springsecurity.security.jwt.JwtAuthTokenProvider;
import kr.carrot.springsecurity.security.jwt.Role;
import kr.carrot.springsecurity.app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final JwtAuthTokenProvider jwtAuthTokenProvider;
    private final UserRepository userRepository;

    private final static long ACCESS_TOKEN_VALID_TIME = 1000L * 60 * 30; // 30분
    private final static long REFRESH_TOKEN_VALID_TIME = 1000L * 60 * 60 * 3; // 3시간

    @Transactional
    public TokenResponseDto login(String username, String password) {

        UserEntity userEntity = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("username not found"));

        String encryptedPassword = passwordEncoder.encode(userEntity.getPassword());
        if (!userEntity.verifyPassword(encryptedPassword)) {
            // password 불일치
            throw new PasswordIncorrectException("password incorrect");
        }

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
