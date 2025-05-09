package com.bookstore.users.controller;

import com.bookstore.users.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @PutMapping("/{email}")
    public ResponseEntity<?> makeUserAdmin(@PathVariable(name = "email") @Valid String email) {
        userService.makeUserAdmin(email);
        return ResponseEntity.ok("User role added");
    }
}
