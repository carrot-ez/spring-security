package kr.carrot.springsecurity.app.dto;

import kr.carrot.springsecurity.security.jwt.Role;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserDto {

    private String username;
    private Role[] roles;

    @Builder
    private UserDto(String id, String username, Role[] roles) {
        this.username = username;
        this.roles = roles;
    }
}
