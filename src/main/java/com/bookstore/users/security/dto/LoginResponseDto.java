package com.bookstore.users.security.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder(setterPrefix = "with")
public class LoginResponseDto {
    private String username;
    private String accessToken;
    private String refreshToken;
}
