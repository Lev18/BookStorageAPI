package com.bookstore.users.service;

import com.bookstore.users.exception.UnauthorizedException;
import com.bookstore.users.security.dto.LoginRequestDto;
import com.bookstore.users.security.dto.LoginResponseDto;
import com.bookstore.users.security.util.JwtUtil;
import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Configuration
@Service
@RequiredArgsConstructor
@EnableMethodSecurity
@Slf4j
public class AuthenticationService {
   private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public LoginResponseDto authenticate(@Valid LoginRequestDto loginRequestDto) {
        try {
            final Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequestDto.getLogin(),
                            loginRequestDto.getPassword()
                    )
            );

            final UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            final String accessToken = jwtUtil.generateAccessToken(userDetails);
            final String refreshToken = jwtUtil.generateRefreshToken(userDetails);

            return LoginResponseDto.builder()
                    .withUsername(userDetails.getUsername())
                    .withAccessToken(accessToken)
                    .withRefreshToken(refreshToken)
                    .build();
        } catch (BadCredentialsException e) {
            throw new UnauthorizedException("Invalid username or password");
        } catch (Exception e) {
            log.error("Unexpected error during authentication: {}", e.getMessage(), e);
            throw new UnauthorizedException("An unexpected error occurred");
        }
    }
}
