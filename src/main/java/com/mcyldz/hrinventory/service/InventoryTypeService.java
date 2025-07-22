package com.mcyldz.hrinventory.service;

import com.mcyldz.hrinventory.dto.request.InventoryTypeCreateRequest;
import com.mcyldz.hrinventory.dto.request.InventoryTypeUpdateRequest;
import com.mcyldz.hrinventory.dto.response.InventoryTypeResponse;

import java.util.List;
import java.util.UUID;

public interface InventoryTypeService {

    InventoryTypeResponse getTypeById(UUID id);

    List<InventoryTypeResponse> getAllTypes();

    InventoryTypeResponse createType(InventoryTypeCreateRequest request);

    InventoryTypeResponse updateType(UUID id, InventoryTypeUpdateRequest request);

    void deleteType(UUID id);
}

