package kr.carrot.springsecurity.service;

import kr.carrot.springsecurity.dto.UserDto;
import kr.carrot.springsecurity.security.jwt.AuthToken;
import kr.carrot.springsecurity.security.jwt.JwtAuthTokenProvider;
import kr.carrot.springsecurity.security.jwt.Role;
import kr.carrot.springsecurity.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtAuthTokenProvider jwtAuthTokenProvider;
    private final UserRepository userRepository;

    private final static long LOGIN_RETENTION_MINUTES = 30;

    public Optional<UserDto> login(String username, String password) {

        // User 정보를 찾을 수 없음
        userRepository.findByUsername(username)
                .orElseThrow(() -> new NoSuchElementException("username을 찾을 수 없습니다."));

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

        UserDto userDto = new UserDto();
        userDto.setUsername(username);
        userDto.setRole(role);

        return Optional.ofNullable(userDto);
    }

    public AuthToken createToken(UserDto userDto) {

        Date expiredDate = Date.from(LocalDateTime.now().plusMinutes(LOGIN_RETENTION_MINUTES).atZone(ZoneId.systemDefault()).toInstant());
        return jwtAuthTokenProvider.createAuthToken(userDto.getUsername(), userDto.getRole().getCode(), expiredDate);
    }
}
