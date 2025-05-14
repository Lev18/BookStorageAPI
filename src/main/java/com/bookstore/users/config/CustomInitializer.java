package com.bookstore.users.config;

import com.bookstore.users.entity.*;
import com.bookstore.users.enums.PermissionTypes;
import com.bookstore.users.enums.RoleTypes;
import com.bookstore.users.repository.PermissionRepository;
import com.bookstore.users.repository.RoleRepository;
import com.bookstore.users.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

@Configuration
@RequiredArgsConstructor
public class CustomInitializer {
    private final String ADMIN = "super_admin";
    private final UserRepository userRepository;
    private final PermissionRepository permissionRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    @Order(1)
    public CommandLineRunner initPermissions() {
        return args -> {
            for (PermissionTypes types : PermissionTypes.values()) {
                permissionRepository.findByPermissionType(types)
                        .orElseGet(() -> {
                    Permission permission = new Permission();
                    permission.setPermissionType(types);
                    return permissionRepository.save(permission);
                });
            }
        };
    }

    @Bean
    @Order(2)
    public CommandLineRunner initRoles() {
        return args -> {
            for (RoleTypes types : RoleTypes.values()) {
                roleRepository.findByRoleTypes(types)
                        .orElseGet(() -> {
                    Role role = new Role();
                    role.setRoleTypes(types);
                    return roleRepository.save(role);
                });
            }
        };
    }

    @Bean
    @Order(3)
    public CommandLineRunner createSuperAdmin() {
        return args -> {
            if (!userRepository.existsByUsername(ADMIN)) {
                Role role = roleRepository.findByRoleTypes(RoleTypes.ROLE_SUPER_ADMIN)
                        .orElseThrow(RuntimeException::new);

                Permission permission = permissionRepository.findByPermissionType(PermissionTypes.CAN_DO_EVERYTHING)
                        .orElseThrow(EntityNotFoundException::new);

                String password = UUID.randomUUID().toString().substring(0, 12);

                User user = new User();
                user.setUsername(ADMIN);
                user.setPassword(passwordEncoder.encode(password));
                user.setEmail("super_admin@book.com");
                user.setEnabled(true);

                UserRole userRole = new UserRole();
                userRole.setRole(role);
                userRole.setUser(user);

                UserPermission userPermission = new UserPermission();
                userPermission.setPermission(permission);
                userPermission.setUser(user);

                user.getUserRole().add(userRole);
                user.getUserPermissions().add(userPermission);

                System.out.println("⚠\uFE0F Admin user created:");
                System.out.println(" UserName: super_admin");
                System.out.println(" Password: " + password);
                System.out.println("⚠\uFE0F Please save this password: It will not be shown again");
                userRepository.save(user);
            }
        };
    }
}
