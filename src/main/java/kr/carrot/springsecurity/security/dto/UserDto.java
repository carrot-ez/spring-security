package kr.carrot.springsecurity.security.dto;

import kr.carrot.springsecurity.security.jwt.Role;
import lombok.Data;

@Data
public class UserDto {

    private String username;
    private Role role;
}
