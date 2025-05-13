package com.bookstore.users.util;

import com.bookstore.users.enums.PermissionTypes;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserPermission {
    @NotEmpty(message = "Permission list can't be empty")
    private List<@NotNull(message = "Permission can't be null") PermissionTypes> permissions;
}
