package com.bookstore.users.security.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequestDto {
    @NotNull
    @NotBlank
    private String login;

    @NotNull
    @NotBlank
    @Size(min = 8)
    private String password;


}
