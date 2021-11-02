package kr.carrot.springsecurity.app.service;

import kr.carrot.springsecurity.app.dto.LoginDto;
import kr.carrot.springsecurity.app.dto.request.TokenRequestDto;
import kr.carrot.springsecurity.app.dto.response.TokenResponseDto;
import kr.carrot.springsecurity.app.entity.*;
import kr.carrot.springsecurity.app.repository.*;
import kr.carrot.springsecurity.security.exception.PasswordIncorrectException;
import kr.carrot.springsecurity.security.exception.RefreshTokenValidException;
import kr.carrot.springsecurity.security.jwt.JwtAuthenticationProvider;
import kr.carrot.springsecurity.security.jwt.TokenType;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final JwtAuthenticationProvider jwtAuthTokenProvider;
    private final UserRepository userRepository;
    private final ClientRepository clientRepository;
    private final TokenRepository tokenRepository;
    private final AuthorizationCodeRepository authorizationCodeRepository;
    private final SessionRepository sessionRepository;

    @Transactional
    public String login(LoginDto loginDto) {

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

        // create session
        SessionEntity sessionEntity = SessionEntity.builder()
                .sessionId(UUID.randomUUID().toString())
                .username(username)
                .clientId(loginDto.getClientId())
                .build();
        sessionRepository.save(sessionEntity);

        // create authorization code
        AuthorizationCodeEntity codeEntity = AuthorizationCodeEntity.builder()
                .code(UUID.randomUUID().toString())
                .clientId(loginDto.getClientId())
                .username(username)
                .sessionId(sessionEntity.getSessionId())
                .expireDate(LocalDateTime.now().plusMinutes(3L))
                .build();
        authorizationCodeRepository.save(codeEntity);

        // check code

//        // generate tokens
//        String accessToken = jwtAuthTokenProvider.createAuthToken(userEntity.getUsername(), loginDto.getClientId(), TokenType.ACCESS_TOKEN).getToken();
//        String refreshToken = jwtAuthTokenProvider.createAuthToken(userEntity.getUsername(), loginDto.getClientId(), TokenType.REFRESH_TOKEN).getToken();
//
//        // save refresh token
//        TokenEntity tokenEntity = TokenEntity.builder()
//                .refreshToken(refreshToken)
//                .clientId(loginDto.getClientId())
//                .build();
//
//        tokenRepository.save(tokenEntity);

        return codeEntity.getCode();
    }

    @Transactional
    public TokenResponseDto accessToken(TokenRequestDto requestDto) {

        // check client info
        String clientId = requestDto.getClientId();
        ClientEntity clientEntity = clientRepository.findByClientId(clientId)
                .orElseThrow();

        if (!clientEntity.getClientSecret().equals(requestDto.getClientSecret())) {
            throw new RuntimeException("invalid client info"); // TODO: change exception type
        }

        // check authorization code
        AuthorizationCodeEntity codeEntity = authorizationCodeRepository.findByCode(requestDto.getCode())
                .orElseThrow();

        if (codeEntity.isExpired()) {
            throw new RuntimeException("authorization code is expired"); // TODO: change exception type
        }
        if (!codeEntity.isSameClient(clientId)) {
            throw new RuntimeException("diff clientId and authorization code");
        }

        // generate tokens
        String accessToken = jwtAuthTokenProvider.createAuthToken(codeEntity.getSessionId(), TokenType.ACCESS_TOKEN).getToken();
        String refreshToken = jwtAuthTokenProvider.createAuthToken(codeEntity.getSessionId(), TokenType.REFRESH_TOKEN).getToken();

        // save refresh token
        TokenEntity tokenEntity = TokenEntity.builder()
                .refreshToken(refreshToken)
                .clientId(clientId)
                .build();

        tokenRepository.save(tokenEntity);

        return new TokenResponseDto(accessToken, refreshToken, null, null);
    }

    @Transactional
    public TokenResponseDto refreshToken(TokenRequestDto requestDto) {

        // check client info
        String clientId = requestDto.getClientId();
        ClientEntity clientEntity = clientRepository.findByClientId(clientId)
                .orElseThrow();

        if (!clientEntity.getClientSecret().equals(requestDto.getClientSecret())) {
            throw new RuntimeException("invalid client info"); // TODO: change exception type
        }

        // check refresh token
        String refreshToken = requestDto.getRefreshToken();
        TokenEntity refreshTokenEntity = tokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow();
        String savedRefreshToken = refreshTokenEntity.getRefreshToken();

        // compare refresh tokens (client <-> server)
        if (!savedRefreshToken.equals(refreshToken)) {
            throw new RefreshTokenValidException("token refreshing failed");
        }

        // send new access token / current refresh token
        String accessToken = jwtAuthTokenProvider.refreshingAccessToken(refreshToken, requestDto.getClientId()).getToken();
        return new TokenResponseDto(accessToken, refreshToken, null, null);
    }
}
