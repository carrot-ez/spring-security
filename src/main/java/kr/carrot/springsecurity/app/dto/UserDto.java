package kr.carrot.springsecurity.app.dto;

import kr.carrot.springsecurity.security.jwt.Role;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
public class UserDto {

    private String username;
    private String email;
    private List<Role> authorities;

    public UserDto(String username, String email, List<Role> authorities) {
        this.username = username;
        this.email = email;
        this.authorities = authorities;
    }
}
