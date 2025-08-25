package com.mcyldz.hrinventory.controller;

import com.mcyldz.hrinventory.dto.request.InventoryTypeCreateRequest;
import com.mcyldz.hrinventory.dto.request.InventoryTypeUpdateRequest;
import com.mcyldz.hrinventory.dto.response.ApiResponse;
import com.mcyldz.hrinventory.dto.response.InventoryTypeResponse;
import com.mcyldz.hrinventory.service.InventoryTypeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/inventory-types")
public class InventoryTypeController {

    private final InventoryTypeService inventoryTypeService;

    public InventoryTypeController(InventoryTypeService inventoryTypeService) {
        this.inventoryTypeService = inventoryTypeService;
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<InventoryTypeResponse>> getTypeById(@PathVariable UUID id) {
        InventoryTypeResponse type = inventoryTypeService.getTypeById(id);
        return ResponseEntity.ok(ApiResponse.success("Inventory type retrieved successfully", type));
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<List<InventoryTypeResponse>>> getAllTypes(
            @RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(name = "size", required = false, defaultValue = "20") Integer size
    ) {
        List<InventoryTypeResponse> types = inventoryTypeService.getAllTypes(page, size);
        return ResponseEntity.ok(ApiResponse.success("All inventory types retrieved successfully", types));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'INVENTORY_MANAGER')")
    public ResponseEntity<ApiResponse<InventoryTypeResponse>> createType(@Valid @RequestBody InventoryTypeCreateRequest request) {
        InventoryTypeResponse createdType = inventoryTypeService.createType(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Inventory type created successfully", createdType));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'INVENTORY_MANAGER')")
    public ResponseEntity<ApiResponse<InventoryTypeResponse>> updateType(
            @PathVariable UUID id,
            @Valid @RequestBody InventoryTypeUpdateRequest request) {
        InventoryTypeResponse updatedType = inventoryTypeService.updateType(id, request);
        return ResponseEntity.ok(ApiResponse.success("Inventory type updated successfully", updatedType));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'INVENTORY_MANAGER')")
    public ResponseEntity<Void> deleteType(@PathVariable UUID id) {
        inventoryTypeService.deleteType(id);
        return ResponseEntity.noContent().build();
    }

}
