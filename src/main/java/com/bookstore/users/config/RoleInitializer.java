package com.bookstore.users.config;

import com.bookstore.users.entity.Role;
import com.bookstore.users.enums.RoleTypes;
import com.bookstore.users.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RoleInitializer implements CommandLineRunner {
    private final RoleRepository repository;
    @Override
    public void run(String... args) throws Exception {
        for (RoleTypes roleTypes : RoleTypes.values()) {
            repository.findByRoleTypes(roleTypes).orElseGet(()  ->{
                Role role = new Role();
                role.setRoleTypes(roleTypes);
               return repository.save(role);
            });
        }
    }
}
