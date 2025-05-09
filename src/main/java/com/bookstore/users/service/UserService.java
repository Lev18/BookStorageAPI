package com.bookstore.users.service;

import com.bookstore.users.entity.Role;
import com.bookstore.users.entity.User;
import com.bookstore.users.entity.UserRole;
import com.bookstore.users.enums.RoleTypes;
import com.bookstore.users.repository.RoleRepository;
import com.bookstore.users.repository.UserRepository;
import com.bookstore.users.repository.UserRoleRepository;
import com.bookstore.users.service.dto.RegisterRequestDto;
import com.bookstore.users.service.dto.RegisterResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;

    public RegisterResponseDto register(RegisterRequestDto requestDto) {
        String email = requestDto.getEmail().toLowerCase().trim();
        if (userRepository.existsByEmail(email)) {
            //TODO: User already exist
            throw new RuntimeException("User already exist");
        }

        User newUser = new User();
        newUser.setUsername(requestDto.getUsername());
        newUser.setEnabled(true);
        newUser.setEmail(requestDto.getEmail());
        newUser.setPassword(passwordEncoder.encode(requestDto.getPassword()));

        Role role = roleRepository.findByRoleTypes(RoleTypes.ROLE_USER)
                .orElseThrow(()->new RuntimeException("Role not found"));

        UserRole userRole = new UserRole();
        userRole.setUser(newUser);
        userRole.setRole(role);

        newUser.getUserRole().add(userRole);

        userRepository.save(newUser);

        return new RegisterResponseDto(newUser.getId(),
                                        newUser.getUsername(),
                                        newUser.getEmail(),
                                        role.getRoleTypes().name());
    }

    public void makeUserAdmin(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(()->new RuntimeException("User not found"));
        Role role =roleRepository.findByRoleTypes(RoleTypes.ROLE_ADMIN).orElseThrow(()->new RuntimeException("Role not found"));
        UserRole userRole = new UserRole();
        userRole.setUser(user);
        userRole.setRole(role);

        userRoleRepository.save(userRole);
    }
}
