package com.mcyldz.hrinventory.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class InventoryUpdateRequest {

    @NotBlank(message = "Brand cannot be blank")
    private String brand;

    @NotBlank(message = "Model cannot be blank")
    private String model;

    @NotBlank(message = "Entry date cannot be null")
    private LocalDate entryDate;

    @NotBlank(message = "Inventory type ID cannot be null")
    private UUID inventoryId;

    @NotBlank(message = "Inventory status ID cannot be null")
    private UUID currentStatusId;
}
