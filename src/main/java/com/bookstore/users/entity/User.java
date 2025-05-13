package com.bookstore.users.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_name", nullable = false)
    private String username;

    @Column(name = "email", nullable = false)
    private String email;

        @Column(name = "password", nullable = false)
        private String password;

        @Column(name = "enabled", nullable = false)
        private boolean enabled = true;

        @OneToMany(mappedBy = "user",
                cascade = CascadeType.PERSIST,
                orphanRemoval = true,
        fetch = FetchType.LAZY)
        private Set<UserRole> userRole = new HashSet<>();

        @OneToMany(mappedBy = "user",  fetch = FetchType.LAZY,
                    cascade = CascadeType.PERSIST,
                    orphanRemoval = true)
        private Set<UserPermission> userPermissions = new HashSet<>();


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Set<SimpleGrantedAuthority> authorities = new HashSet<>();

        for (UserRole role : userRole){
            authorities.add(new SimpleGrantedAuthority(role.getRole()
                    .getRoleTypes().name()));
        }

        for (UserPermission permission : userPermissions) {
            authorities.add(new SimpleGrantedAuthority(permission.getPermission()
                    .getPermissionType().name()));
        }
        return authorities;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }

}
