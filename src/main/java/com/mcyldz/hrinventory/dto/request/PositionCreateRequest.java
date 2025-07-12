package com.mcyldz.hrinventory.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PositionCreateRequest {

    @NotBlank(message = "Position name cannot be blank")
    @Size(max = 100, message = "Position name can have a maximum of 100 characters")
    private String name;

    @Size(max = 255, message = "Description can have a maximum of 255 characters")
    private String description;
}
