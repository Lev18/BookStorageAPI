package com.bookstore.users.service;

import com.bookstore.users.entity.Role;
import com.bookstore.users.entity.User;
import com.bookstore.users.entity.UserRole;
import com.bookstore.users.enums.RoleTypes;
import com.bookstore.users.repository.RoleRepository;
import com.bookstore.users.repository.UserRepository;
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

    public RegisterResponseDto register(RegisterRequestDto requestDto) {
        String email = requestDto.getEmail().toLowerCase().trim();
        if (userRepository.existsByEmail(email)) {
            //TODO: User already exist
            throw new RuntimeException();
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

        userRepository.save(newUser);

        return new RegisterResponseDto(newUser.getId(),
                                        newUser.getUsername(),
                                        newUser.getEmail(),
                                        role.getRoleTypes().name());
    }
}
