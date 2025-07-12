package com.mcyldz.hrinventory.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class InventoryTypeCreateRequest {

    @NotBlank(message = "Inventory type name cannot be blank")
    @Size(max = 50, message = "Name can have a maximum of 50 characters")
    private String name;

    @Size(max = 255, message = "Description can have a maximum of 255 characters")
    private String description;
}
