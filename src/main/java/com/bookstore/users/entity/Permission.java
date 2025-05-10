package com.bookstore.users.entity;

import com.bookstore.users.enums.PermissionTypes;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Entity
@Table(name = "permissions")
@Getter
@Setter
public class Permission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    @Enumerated(value = EnumType.STRING)
    private PermissionTypes permissionTypes;

    @OneToMany(mappedBy = "permission", fetch = FetchType.LAZY,
                cascade = CascadeType.PERSIST,
                orphanRemoval = true)
    private Set<UserPermission> userPermissions;
}
