package com.samoilov.project.antifraud.entity;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.samoilov.project.antifraud.enums.Authority;
import com.samoilov.project.antifraud.enums.LockState;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@ToString(exclude = "encodedPassword")
@Table(name = "user", uniqueConstraints = {
        @UniqueConstraint(columnNames = "username", name = "unique_username_constraint")
})
public class UserEntity implements UserDetails {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "password", nullable = false)
    private String encodedPassword;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Authority role;

    @Enumerated(EnumType.STRING)
    @Column(name = "lock_state", nullable = false)
    private LockState lockState;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(role);
    }

    @Override
    public String getPassword() {
        return encodedPassword;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.lockState.equals(LockState.UNLOCK);
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}