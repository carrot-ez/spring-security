package kr.carrot.springsecurity.app.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class LoginDto {

    @NotBlank
    private String username;
    @NotBlank
    private String password;
    @NotBlank
    private String clientId;
}
