package kr.carrot.springsecurity.app.service;

import kr.carrot.springsecurity.app.dto.LoginDto;
import kr.carrot.springsecurity.app.dto.response.TokenResponseDto;
import kr.carrot.springsecurity.app.entity.TokenEntity;
import kr.carrot.springsecurity.app.entity.UserEntity;
import kr.carrot.springsecurity.app.repository.TokenRepository;
import kr.carrot.springsecurity.security.exception.InvalidJwtTokenException;
import kr.carrot.springsecurity.security.exception.PasswordIncorrectException;
import kr.carrot.springsecurity.security.jwt.*;
import kr.carrot.springsecurity.app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final JwtAuthenticationProvider jwtAuthTokenProvider;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;

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
                .authorities(new Role[] {Role.USER, Role.UNKNOWN})
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
        String salt = UUID.randomUUID().toString();
        String accessToken = jwtAuthTokenProvider.createAuthToken(userEntity.getUsername(), salt, TokenType.ACCESS_TOKEN).getToken();
        String refreshToken = jwtAuthTokenProvider.createAuthToken(userEntity.getUsername(), salt, TokenType.REFRESH_TOKEN).getToken();

        // save refresh token
        TokenEntity tokenEntity = TokenEntity.builder()
                .refreshToken(refreshToken)
                .salt(salt)
                .build();

        tokenRepository.save(tokenEntity);

        return new TokenResponseDto(accessToken, refreshToken);
    }

    @Transactional
    public TokenResponseDto refreshingToken(Optional<String> token) {

        if (token.isPresent()) {

            // find refresh token in db
            String refreshToken = token.get();
            TokenEntity refreshTokenEntity = tokenRepository.findByRefreshToken(refreshToken)
                    .orElseThrow();
            String savedRefreshToken = refreshTokenEntity.getRefreshToken();
            String salt = refreshTokenEntity.getSalt();

            // compare refresh tokens (client <-> server)
            if (savedRefreshToken.equals(refreshToken)) {

                // success to match tokens -> send new access token / current refresh token
                String accessToken = jwtAuthTokenProvider.refreshingAccessToken(refreshToken, salt).getToken();
                return new TokenResponseDto(accessToken, refreshToken);
            }
        }

        throw new InvalidJwtTokenException("refreshing token");
    }
}
