package com.mcyldz.hrinventory.controller;

import com.mcyldz.hrinventory.dto.request.AssignmentCreateRequest;
import com.mcyldz.hrinventory.dto.request.AssignmentReturnRequest;
import com.mcyldz.hrinventory.dto.response.ApiResponse;
import com.mcyldz.hrinventory.dto.response.AssignmentResponse;
import com.mcyldz.hrinventory.service.AssignmentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/assignments")
public class AssignmentController {

    private final AssignmentService assignmentService;

    public AssignmentController(AssignmentService assignmentService) {
        this.assignmentService = assignmentService;
    }

    @GetMapping("/personnel/{personnelId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    public ResponseEntity<ApiResponse<List<AssignmentResponse>>> getAssignmentsByPersonnel(
            @PathVariable UUID personnelId) {

        List<AssignmentResponse> assignments = assignmentService.getAssignmentsByPersonnelId(personnelId);
        return ResponseEntity.ok(ApiResponse.success("Active assignments for personnel retrieved successfully", assignments));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    public ResponseEntity<ApiResponse<List<AssignmentResponse>>> createAssignments(
            @Valid @RequestBody AssignmentCreateRequest request) {

        List<AssignmentResponse> createdAssignments = assignmentService.createAssignments(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Assignments created successfully", createdAssignments));
    }

    @PutMapping("/return")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    public ResponseEntity<ApiResponse<List<AssignmentResponse>>> returnAssignments(
            @Valid @RequestBody AssignmentReturnRequest request) {

        List<AssignmentResponse> returnedAssignments = assignmentService.returnAssignments(request);
        return ResponseEntity.ok(ApiResponse.success("Assignments returned successfully", returnedAssignments));
    }
}
