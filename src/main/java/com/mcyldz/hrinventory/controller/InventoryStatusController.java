package com.mcyldz.hrinventory.controller;

import com.mcyldz.hrinventory.dto.request.InventoryStatusCreateRequest;
import com.mcyldz.hrinventory.dto.request.InventoryStatusUpdateRequest;
import com.mcyldz.hrinventory.dto.response.ApiResponse;
import com.mcyldz.hrinventory.dto.response.InventoryStatusResponse;
import com.mcyldz.hrinventory.service.InventoryStatusService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/inventory-statuses")
public class InventoryStatusController {

    private final InventoryStatusService inventoryStatusService;

    public InventoryStatusController(InventoryStatusService inventoryStatusService) {
        this.inventoryStatusService = inventoryStatusService;
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<List<InventoryStatusResponse>>> getAllStatuses() {
        List<InventoryStatusResponse> statuses = inventoryStatusService.getAllStatuses();
        return ResponseEntity.ok(ApiResponse.success("All inventory statuses retrieved successfully", statuses));
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<InventoryStatusResponse>> getStatusById(@PathVariable UUID id) {
        InventoryStatusResponse status = inventoryStatusService.getStatusById(id);
        return ResponseEntity.ok(ApiResponse.success("Inventory status retrieved successfully", status));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'INVENTORY_MANAGER')")
    public ResponseEntity<ApiResponse<InventoryStatusResponse>> createStatus(@Valid @RequestBody InventoryStatusCreateRequest request) {
        InventoryStatusResponse createdStatus = inventoryStatusService.createStatus(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Inventory status created successfully", createdStatus));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'INVENTORY_MANAGER')")
    public ResponseEntity<ApiResponse<InventoryStatusResponse>> updateStatus(
            @PathVariable UUID id,
            @Valid @RequestBody InventoryStatusUpdateRequest request) {
        InventoryStatusResponse updatedStatus = inventoryStatusService.updateStatus(id, request);
        return ResponseEntity.ok(ApiResponse.success("Inventory status updated successfully", updatedStatus));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'INVENTORY_MANAGER')")
    public ResponseEntity<Void> deleteStatus(@PathVariable UUID id) {
        inventoryStatusService.deleteStatus(id);
        return ResponseEntity.noContent().build();
    }

}
