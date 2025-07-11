package com.mcyldz.hrinventory.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
public class AssignmentCreateRequest {

    @NotNull(message = "Personnel ID cannot be null")
    private UUID personnelId;

    @NotNull(message = "Inventory item ID list cannot be null")
    private List<UUID> inventoryItemIds;

    @NotNull(message = "Assignment date cannot be null")
    private LocalDate assignmentDate;

    private String notes;

}
