package com.bookstore.users.controller;

import com.bookstore.users.security.dto.LoginRequestDto;
import com.bookstore.users.security.dto.LoginResponseDto;
import com.bookstore.users.service.AuthenticationService;
import com.bookstore.users.service.UserService;
import com.bookstore.users.service.dto.RegisterRequestDto;
import com.bookstore.users.service.dto.RegisterResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final UserService userService;
    
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto>login(@Valid @RequestBody LoginRequestDto loginRequestDto) {
       return ResponseEntity.ok(authenticationService.authenticate(loginRequestDto));
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponseDto>  register(@Valid @RequestBody RegisterRequestDto requestDto) {
        return ResponseEntity.ok(userService.register(requestDto));
    }
}
