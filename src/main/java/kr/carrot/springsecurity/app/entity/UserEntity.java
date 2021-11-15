package kr.carrot.springsecurity.app.entity;

import kr.carrot.springsecurity.security.jwt.Role;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

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
    private List<Role> authorities = new ArrayList<>();

    @Column(nullable = false, length = 100)
    private String email;

    @Builder
    private UserEntity(Long id, String username, String password, String email, Role[] authorities) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.authorities = Arrays.asList(authorities);
    }

    public Set<SimpleGrantedAuthority> getGrantedAuthorities() {
        return authorities.stream()
                .map(e -> new SimpleGrantedAuthority(e.getCode()))
                .collect(Collectors.toSet());
    }
}
