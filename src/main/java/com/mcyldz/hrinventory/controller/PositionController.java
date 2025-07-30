package com.mcyldz.hrinventory.controller;

import com.mcyldz.hrinventory.dto.request.PositionCreateRequest;
import com.mcyldz.hrinventory.dto.request.PositionUpdateRequest;
import com.mcyldz.hrinventory.dto.response.ApiResponse;
import com.mcyldz.hrinventory.dto.response.PositionResponse;
import com.mcyldz.hrinventory.service.PositionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/positions")
public class PositionController {

    private final PositionService positionService;

    public PositionController(PositionService positionService) {
        this.positionService = positionService;
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<PositionResponse>> getPositionById(@PathVariable UUID id) {
        PositionResponse position = positionService.getPositionById(id);
        return ResponseEntity.ok(ApiResponse.success("Position retrieved successfully", position));
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<List<PositionResponse>>> getAllPositions() {
        List<PositionResponse> positions = positionService.getAllPositions();
        return ResponseEntity.ok(ApiResponse.success("All positions retrieved successfully", positions));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<PositionResponse>> createPosition(@Valid @RequestBody PositionCreateRequest request) {
        PositionResponse createdPosition = positionService.createPosition(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Position created successfully", createdPosition));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<PositionResponse>> updatePosition(
            @PathVariable UUID id,
            @Valid @RequestBody PositionUpdateRequest request) {
        PositionResponse updatedPosition = positionService.updatePosition(id, request);
        return ResponseEntity.ok(ApiResponse.success("Position updated successfully", updatedPosition));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deletePosition(@PathVariable UUID id) {
        positionService.deletePosition(id);
        return ResponseEntity.noContent().build();
    }
}
