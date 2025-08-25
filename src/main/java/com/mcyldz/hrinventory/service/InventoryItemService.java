package com.mcyldz.hrinventory.service;

import com.mcyldz.hrinventory.dto.request.InventoryCreateRequest;
import com.mcyldz.hrinventory.dto.request.InventoryUpdateRequest;
import com.mcyldz.hrinventory.dto.response.InventoryItemResponse;
import com.mcyldz.hrinventory.dto.response.InventoryStatusHistoryResponse;
import com.mcyldz.hrinventory.entity.InventoryStatusHistory;

import java.util.List;
import java.util.UUID;

public interface InventoryItemService {

    InventoryItemResponse getInventoryItemById(UUID id);

    List<InventoryItemResponse> getAllInventoryItems(Integer page, Integer size);

    InventoryItemResponse createInventoryItem(InventoryCreateRequest request);

    InventoryItemResponse updateInventoryItem(UUID id, InventoryUpdateRequest request);

    void deleteInventoryItem(UUID id);

    List<InventoryStatusHistoryResponse> getInventoryStatusHistories(UUID itemId);
}
