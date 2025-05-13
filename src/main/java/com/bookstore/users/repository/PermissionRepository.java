package com.bookstore.users.repository;

import com.bookstore.users.entity.Permission;
import com.bookstore.users.enums.PermissionTypes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {
    Optional<Permission> findByPermissionType(PermissionTypes permissionTypes);
}
