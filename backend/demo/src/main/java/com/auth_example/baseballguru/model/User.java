package com.auth_example.baseballguru.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

// By specifying this class as an entity, this class will map directly to the users table in our database
@Entity
@Table(name="users")
@Getter
@Setter
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(unique=true, nullable=false)
    private String username;

    @Column(unique=true, nullable=false)
    private String email;

    @Column(nullable=true)
    private String password;

    private boolean enabled;

    @Column(name="verification_code")
    private String verificationCode;

    @Column(name="verification_expiration")
    private LocalDateTime verificationCodeExpiresAt;

    // NEW FOR OAUTH2
    @Column(name="provider")
    private String provider;

    @Column(name="provider_id")
    private String providerId;

    @Enumerated(EnumType.STRING)
    @Column(nullable=false)
    private Role role;

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.provider = "local";
        this.role = Role.USER;
    }

    public User(String username, String email, String provider, String providerId) {
        this.username = username;
        this.email = email;
        this.provider = provider;
        this.providerId = providerId;
        this.enabled = true; // OAuth2 users are auto-enabled since Google verified them
        this.role = Role.USER;
    }

    public User() {}

    // would return role list but empty list returned since role based authentication isn't the focus
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    // vvv once again just assumed for all 4

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override public boolean isAccountNonLocked() {
        return true;
    }

    @Override public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override public boolean isEnabled() {
        return enabled;
    }

    // OAuth2 helper methods
    public boolean isOAuth2User() {
        return !"local".equals(provider);
    }
    public boolean hasPassword() {
        return password != null && !password.isEmpty();
    }

}
