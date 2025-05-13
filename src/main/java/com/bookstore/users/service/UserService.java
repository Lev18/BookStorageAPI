package com.bookstore.users.service;

import com.bookstore.users.entity.*;
import com.bookstore.users.enums.PermissionTypes;
import com.bookstore.users.enums.RoleTypes;
import com.bookstore.users.repository.*;
import com.bookstore.users.service.dto.RegisterRequestDto;
import com.bookstore.users.service.dto.RegisterResponseDto;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;
    private final PermissionRepository permissionRepository;
    private final UserPermissionRepository userPermissionRepository;

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
        Permission permission = permissionRepository.findByPermissionType(PermissionTypes.CAN_GET)
                .orElseThrow(EntityNotFoundException::new);

        UserRole userRole = new UserRole();
        userRole.setUser(newUser);
        userRole.setRole(role);

        UserPermission userPermission = new UserPermission();
        userPermission.setUser(newUser);
        userPermission.setPermission(permission);

        newUser.getUserRole().add(userRole);
        newUser.getUserPermissions().add(userPermission);

        userRepository.save(newUser);

        return new RegisterResponseDto(newUser.getId(),
                                        newUser.getUsername(),
                                        newUser.getEmail(),
                                        role.getRoleTypes().name());
    }

    public void makeUserAdmin(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(()->new RuntimeException("User not found"));
        Role role = roleRepository.findByRoleTypes(RoleTypes.ROLE_ADMIN)
                .orElseThrow(()->new RuntimeException("Role not found"));
        UserRole userRole = new UserRole();
        userRole.setUser(user);
        userRole.setRole(role);

        userRoleRepository.save(userRole);
    }

    public void deleteUser(@Valid String email) {
    }

    public void addPrivilegesToUser(@Valid String email,
                                    @NotEmpty(message = "Permission list can't be empty")
                                    List<@NotNull(message = "Permission can't be null")
                                            PermissionTypes> permissions) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found" + email));

        Set<String> authorities = user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        permissions.forEach( types -> {
            Permission permission = permissionRepository.findByPermissionType(types)
                    .orElseThrow(()->new EntityNotFoundException("Permission not found" + types));
            if (!authorities.contains(permission.getPermissionType().name())) {
                UserPermission userPermission = new UserPermission();
                userPermission.setUser(user);
                userPermission.setPermission(permission);
                userPermissionRepository.save(userPermission);
            }
        });
    }
}
