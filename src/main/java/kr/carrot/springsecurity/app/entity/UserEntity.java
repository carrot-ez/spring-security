package kr.carrot.springsecurity.app.entity;

import kr.carrot.springsecurity.security.jwt.Role;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity
@Getter
@Table(name = "tb_user")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(nullable = false, length = 100)
    private String password;

    @ElementCollection
    @CollectionTable
    @Enumerated(EnumType.STRING)
    private List<Role> roles = new ArrayList<>();

    @Column(nullable = false, length = 100)
    private String email;

    @Builder
    private UserEntity(Long id, String username, String password, String email, Role[] roles) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.roles = Arrays.asList(roles);
    }

    public String[] getRoles() {
        return roles.stream()
                .map(Role::getCode)
                .toArray(String[]::new);
    }
}
