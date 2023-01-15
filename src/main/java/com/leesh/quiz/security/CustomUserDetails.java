package com.leesh.quiz.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.leesh.quiz.domain.user.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * 유저 인증용 객체와 도메인 객체를 분리시키기 위함
 */
public record CustomUserDetails(String username, @JsonIgnore String password, Role role) implements UserDetails {

    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        return List.of(
                new SimpleGrantedAuthority(role.name())
        );
    }

    @JsonIgnore
    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isEnabled() {
        return true;
    }

    public static CustomUserDetails of(String username, String password, Role role) {
        return new CustomUserDetails(username, password, role);
    }
}
