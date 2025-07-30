package com.mcyldz.hrinventory.controller;

import com.mcyldz.hrinventory.dto.request.InventoryCreateRequest;
import com.mcyldz.hrinventory.dto.request.InventoryUpdateRequest;
import com.mcyldz.hrinventory.dto.response.ApiResponse;
import com.mcyldz.hrinventory.dto.response.InventoryItemResponse;
import com.mcyldz.hrinventory.dto.response.InventoryStatusHistoryResponse;
import com.mcyldz.hrinventory.service.InventoryItemService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "/api/v1/inventory-items")
public class InventoryItemController {

    private final InventoryItemService inventoryItemService;

    public InventoryItemController(InventoryItemService inventoryItemService) {
        this.inventoryItemService = inventoryItemService;
    }

    @GetMapping(path = "/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'INVENTORY_MANAGER')")
    public ResponseEntity<ApiResponse<InventoryItemResponse>> getInventoryItemById(@PathVariable UUID id){
        InventoryItemResponse inventoryItemResponse = inventoryItemService.getInventoryItemById(id);
        return ResponseEntity.ok(ApiResponse.success("Inventory item retrieved successfully", inventoryItemResponse));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'INVENTORY_MANAGER')")
    public ResponseEntity<ApiResponse<List<InventoryItemResponse>>> getAllInventoryItems(){
        List<InventoryItemResponse> inventoryItemResponses = inventoryItemService.getAllInventoryItems();
        return ResponseEntity.ok(ApiResponse.success("All inventory items retrieved successfully", inventoryItemResponses));
    }

    @GetMapping("/{id}/history")
    @PreAuthorize("hasAnyRole('ADMIN', 'INVENTORY_MANAGER')")
    public ResponseEntity<ApiResponse<List<InventoryStatusHistoryResponse>>> getStatusHistory(@PathVariable UUID id) {
        List<InventoryStatusHistoryResponse> history = inventoryItemService.getInventoryStatusHistories(id);
        return ResponseEntity.ok(ApiResponse.success("Status history retrieved successfully", history));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'INVENTORY_MANAGER')")
    public ResponseEntity<ApiResponse<InventoryItemResponse>> createInventoryItem(@Valid @RequestBody InventoryCreateRequest request){
        InventoryItemResponse inventoryItemResponse = inventoryItemService.createInventoryItem(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Inventory item created successfully", inventoryItemResponse));
    }

    @PutMapping(path = "/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'INVENTORY_MANAGER')")
    public ResponseEntity<ApiResponse<InventoryItemResponse>> updateInventoryItem(@PathVariable UUID id, @Valid @RequestBody InventoryUpdateRequest request){
        InventoryItemResponse inventoryItemResponse = inventoryItemService.updateInventoryItem(id, request);
        return ResponseEntity.ok(ApiResponse.success("Inventory item updated successfully", inventoryItemResponse));
    }

    @DeleteMapping(path = "/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'INVENTORY_MANAGER')")
    public ResponseEntity<Void> deleteInventoryItem(@PathVariable UUID id){
        inventoryItemService.deleteInventoryItem(id);
        return ResponseEntity.noContent().build();
    }
}
