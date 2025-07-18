package com.mcyldz.hrinventory.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InventoryStatusHistoryResponse {

    private UUID id;

    private LocalDateTime changeDate;

    private String notes;

    private UUID inventoryItemId;

    private String inventoryItemName;

    private String statusName;

    private String changedByUsername;
}
