package com.bookstore.users.config;

import com.bookstore.users.entity.Role;
import com.bookstore.users.entity.User;
import com.bookstore.users.entity.UserRole;
import com.bookstore.users.enums.RoleTypes;
import com.bookstore.users.repository.RoleRepository;
import com.bookstore.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AdminInitializer implements CommandLineRunner {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    private static final String ADMIN = "super_admin";

    @Override
    public void run(String... args) throws Exception {
        if (!userRepository.existsByUsername(ADMIN)) {
            Role role = roleRepository.findByRoleTypes(RoleTypes.ROLE_SUPER_ADMIN)
                    .orElseThrow(RuntimeException::new);

            String password = UUID.randomUUID().toString().substring(0,12);

            User user = new User();
            user.setUsername(ADMIN);
            user.setPassword(passwordEncoder.encode(password));
            user.setEmail("super_admin@book.com");
            user.setEnabled(true);

            UserRole userRole = new UserRole();
            userRole.setRole(role);
            userRole.setUser(user);
            user.getUserRole().add(userRole);

            userRepository.save(user);

            System.out.println("⚠\uFE0F Admin user created:");
            System.out.println(" UserName: super_admin");
            System.out.println(" Password: " + password);
            System.out.println("⚠\uFE0F Please save this password: It will not be shown again");
        }
    }
}
