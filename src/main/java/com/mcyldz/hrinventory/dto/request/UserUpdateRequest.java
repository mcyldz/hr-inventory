package com.mcyldz.hrinventory.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class UserUpdateRequest {

    @NotNull(message = "Role ID cannot be null")
    private UUID roleId;

    @NotNull(message = "Active status cannot be null")
    private Boolean isActive;
}
