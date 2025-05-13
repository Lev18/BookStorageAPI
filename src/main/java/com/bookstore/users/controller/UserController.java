package com.bookstore.users.controller;

import com.bookstore.users.service.UserService;
import com.bookstore.users.util.UserPermission;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    @PreAuthorize("hasRole('SUPER_ADMIN') or hasAuthority('CAN_UPDATE_USER'")
    @PutMapping("/admin/{email}")
    public ResponseEntity<?> makeUserAdmin(@PathVariable(name = "email") @Valid String email) {
        //TODO: add proper response
        userService.makeUserAdmin(email);
        return ResponseEntity.ok("User role added");
    }

    @DeleteMapping("/{email}")
    @PreAuthorize("hasRole('SUPER_ADMIN') or #email == principal.email or hasAuthority('CAN_DELETE_USER')")
    public  ResponseEntity<?> deleteUser(@PathVariable(name = "email")
                                         @Valid String email) {
        userService.deleteUser(email);
        return ResponseEntity.ok("User deleted successfully");
    }

    @GetMapping("/me")
    public ResponseEntity<String> whoAmI(Authentication authentication) {
        return ResponseEntity.ok("Principal " + authentication.getPrincipal());
    }

    @PutMapping("/permissions/{email}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<?> addPrivilegesToUser(@Valid @PathVariable(name = "email")
                                                     String email,
                                                 @Valid
                                                 @RequestBody UserPermission permission
                                                 ) {
        userService.addPrivilegesToUser(email, permission.getPermissions());
        return ResponseEntity.noContent().build();
    }
}
