package com.bookstore.users.entity;

import com.bookstore.users.enums.PermissionTypes;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@Table(name = "permissions")
@Getter
@Setter
@NoArgsConstructor
public class Permission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    @Enumerated(value = EnumType.STRING)
    private PermissionTypes permissionType;

    @OneToMany(mappedBy = "permission", fetch = FetchType.LAZY,
                cascade = CascadeType.PERSIST,
                orphanRemoval = true)
    private Set<UserPermission> userPermissions;

    public Permission(@NotEmpty(message = "Permission can't be null") PermissionTypes permissionTypes) {
        this.permissionType = permissionTypes;
    }
}
