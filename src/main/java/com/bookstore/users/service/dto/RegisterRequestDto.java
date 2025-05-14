package com.bookstore.users.service.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
public class RegisterRequestDto {
    @NotBlank
    @NotNull
    private String username;

    @NotBlank
    @NotNull
    @Length(min = 8)
    private String password;

    @NotBlank
    @NotNull
    @Email(message = "Invalid email format")
    private String email;
}
