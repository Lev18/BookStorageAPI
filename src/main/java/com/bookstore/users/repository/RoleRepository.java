package com.bookstore.users.repository;

import com.bookstore.users.entity.Role;
import com.bookstore.users.enums.RoleTypes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByRoleTypes(RoleTypes roleTypes);
}
