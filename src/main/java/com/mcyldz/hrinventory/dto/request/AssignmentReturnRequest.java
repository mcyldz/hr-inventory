package com.mcyldz.hrinventory.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
public class AssignmentReturnRequest {

    @NotEmpty(message = "Assignment ID list cannot be empty")
    private List<UUID> assignmentIds;

    @NotNull(message = "Return date cannot be null")
    private LocalDate returnDate;
}
