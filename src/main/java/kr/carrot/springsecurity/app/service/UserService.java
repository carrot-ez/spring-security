package kr.carrot.springsecurity.app.service;

import kr.carrot.springsecurity.app.dto.UserDto;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtAuthTokenProvider jwtAuthTokenProvider;
    private final UserRepository userRepository;

    private final static long ACCESS_TOKEN_VALID_SECOND = 1000L * 30; // 30분
    private final static long REFRESH_TOKEN_VALID_SECOND = 1000L * 60 * 3; // 3시간

    @Transactional
    public Optional<UserDto> login(String username, String password) {

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);

        // check password
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // login success
        SecurityContextHolder.getContext().setAuthentication(authentication);

        Role role = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .map(Role::of)
                .orElse(Role.UNKNOWN);

        // create user dto
        UserDto userDto = UserDto.builder()
                .username(username)
                .roles(new Role[]{role})
                .build();

        return Optional.ofNullable(userDto);
    }

    public AuthToken createAccessToken(UserDto userDto) {

        Date expiredDate = new Date(System.currentTimeMillis() + ACCESS_TOKEN_VALID_SECOND);
        return jwtAuthTokenProvider.createAuthToken(userDto, expiredDate);
    }

    public AuthToken createRefreshToken(UserDto userDto) {

        Date expiredDate = new Date(System.currentTimeMillis() + REFRESH_TOKEN_VALID_SECOND);
        return jwtAuthTokenProvider.createAuthToken(userDto, expiredDate);
    }

}
