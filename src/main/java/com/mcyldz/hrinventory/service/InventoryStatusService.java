package com.mcyldz.hrinventory.service;

import com.mcyldz.hrinventory.dto.request.InventoryStatusCreateRequest;
import com.mcyldz.hrinventory.dto.request.InventoryStatusUpdateRequest;
import com.mcyldz.hrinventory.dto.response.InventoryStatusResponse;

import java.util.List;
import java.util.UUID;

public interface InventoryStatusService {

    InventoryStatusResponse getStatusById(UUID id);

    List<InventoryStatusResponse> getAllStatuses();

    InventoryStatusResponse createStatus(InventoryStatusCreateRequest request);

    InventoryStatusResponse updateStatus(UUID id, InventoryStatusUpdateRequest request);

    void deleteStatus(UUID id);
}
