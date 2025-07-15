package com.mcyldz.hrinventory.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InventoryItemResponse {

    private UUID id;

    private String brand;

    private String model;

    private String serialNumber;

    private LocalDate entryDate;

    private String inventoryTypeName;

    private String currentStatusName;
}
