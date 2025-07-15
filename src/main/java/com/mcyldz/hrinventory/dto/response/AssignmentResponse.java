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
public class AssignmentResponse {

    private UUID id;

    private LocalDate assignmentDate;

    private LocalDate returnDate;

    private String notes;

    private UUID personnelId;

    private String personnelName;

    private UUID inventoryItemId;

    private String inventoryItemName;

    private String assignedByUser;

    private String returnedByUser;
}
