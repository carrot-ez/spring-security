package kr.carrot.springsecurity.security.dto;

import lombok.Data;

@Data
public class LoginDto {

    private String username;
    private String password;
}
