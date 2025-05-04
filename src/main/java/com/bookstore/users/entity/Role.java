package com.bookstore.users.entity;

import com.bookstore.users.enums.RoleTypes;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "roles")
@Getter
@Setter
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "role_type")
    @Enumerated(value = EnumType.STRING)
    private RoleTypes roleTypes;

    @OneToMany(mappedBy = "role",
            cascade = CascadeType.PERSIST,
            orphanRemoval = true)
    private Set<UserRole> userRoles = new HashSet<>();
}
