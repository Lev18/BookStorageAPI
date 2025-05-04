package com.bookstore.users.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@RequiredArgsConstructor
@Getter
public enum RoleTypes {
    ROLE_USER("ROLE_USER"),
    ROLE_ADMIN("ROLE_ADMIN"),
    ROLE_MODERATOR("ROLE_MODERATOR");
    private final  String displayName;

    public static RoleTypes fromLanguageName(String input) {

        if (input == null || input.isBlank()) {
            return RoleTypes.ROLE_USER;
        }
        return Arrays.stream(RoleTypes.values())
                .filter(lang ->lang.displayName.equalsIgnoreCase(input))
                .findFirst()
                .orElse(ROLE_USER);
    }
}
