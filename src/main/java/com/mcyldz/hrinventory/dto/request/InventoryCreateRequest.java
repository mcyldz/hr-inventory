package com.mcyldz.hrinventory.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class InventoryCreateRequest {

    @NotBlank(message = "Brand cannot be blank")
    private String brand;

    @NotBlank(message = "Model cannot be blank")
    private String model;

    @NotBlank(message = "Serial number cannot be blank")
    private String serialNumber;

    @NotNull(message = "Entry date cannot be null")
    private LocalDate entryDate;

    @NotNull(message = "Inventory type ID cannot be null")
    private UUID inventoryTypeId;

    @NotNull(message = "Initial status ID cannot be null")
    private UUID statusId;
}
