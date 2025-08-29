package com.mcyldz.hrinventory.service.impl;

import com.mcyldz.hrinventory.dto.request.InventoryCreateRequest;
import com.mcyldz.hrinventory.dto.request.InventoryUpdateRequest;
import com.mcyldz.hrinventory.dto.response.InventoryItemResponse;
import com.mcyldz.hrinventory.dto.response.InventoryStatusHistoryResponse;
import com.mcyldz.hrinventory.entity.*;
import com.mcyldz.hrinventory.exception.model.DuplicateResourceException;
import com.mcyldz.hrinventory.exception.model.ErrorCode;
import com.mcyldz.hrinventory.exception.model.ResourceNotFoundException;
import com.mcyldz.hrinventory.mapper.InventoryItemMapper;
import com.mcyldz.hrinventory.mapper.InventoryStatusHistoryMapper;
import com.mcyldz.hrinventory.repository.*;
import com.mcyldz.hrinventory.service.InventoryItemService;
import com.mcyldz.hrinventory.util.SecurityUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class InventoryItemServiceImpl implements InventoryItemService {

    private final InventoryItemRepository inventoryItemRepository;

    private final InventoryTypeRepository inventoryTypeRepository;

    private final InventoryStatusRepository inventoryStatusRepository;

    private final InventoryStatusHistoryRepository inventoryStatusHistoryRepository;

    private final InventoryItemMapper inventoryItemMapper;

    private final InventoryStatusHistoryMapper inventoryStatusHistoryMapper;

    private final UserRepository userRepository;


    private final static String DEFAULT_STATUS_NAME = "DEPODA";

    public InventoryItemServiceImpl(InventoryItemRepository inventoryItemRepository, InventoryTypeRepository inventoryTypeRepository, InventoryStatusRepository inventoryStatusRepository, InventoryStatusHistoryRepository inventoryStatusHistoryRepository, InventoryItemMapper inventoryItemMapper, InventoryStatusHistoryMapper inventoryStatusHistoryMapper, UserRepository userRepository) {
        this.inventoryItemRepository = inventoryItemRepository;
        this.inventoryTypeRepository = inventoryTypeRepository;
        this.inventoryStatusRepository = inventoryStatusRepository;
        this.inventoryStatusHistoryRepository = inventoryStatusHistoryRepository;
        this.inventoryItemMapper = inventoryItemMapper;
        this.inventoryStatusHistoryMapper = inventoryStatusHistoryMapper;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public InventoryItemResponse getInventoryItemById(UUID id) {
        InventoryItem inventoryItem = findInventoryItemByIdOrThrow(id);
        return inventoryItemMapper.toInventoryItemResponse(inventoryItem);
    }

    @Override
    @Transactional(readOnly = true)
    public List<InventoryItemResponse> getAllInventoryItems(Integer page, Integer size) {

        Pageable pageable = PageRequest.of(page, size);

        Page<InventoryItem> inventoryItemPage = inventoryItemRepository.findAll(pageable);

        List<InventoryItem> inventoryItems = inventoryItemPage.getContent();

        return inventoryItemMapper.toInventoryItemResponseList(inventoryItems);
    }

    @Override
    @Transactional
    public InventoryItemResponse createInventoryItem(InventoryCreateRequest request) {

        inventoryItemRepository.findBySerialNumber(request.getSerialNumber())
                .ifPresent(item -> {
            throw new DuplicateResourceException(ErrorCode.SERIAL_NUMBER_ALREADY_EXISTS);
        });

        InventoryItem inventoryItem = inventoryItemMapper.toInventoryItem(request);

        InventoryType inventoryType = inventoryTypeRepository.findById(request.getInventoryTypeId()).orElseThrow(()->new ResourceNotFoundException(ErrorCode.INVENTORY_TYPE_NOT_FOUND));

        inventoryItem.setInventoryType(inventoryType);

        InventoryStatus defaultStatus = inventoryStatusRepository.findByName(DEFAULT_STATUS_NAME).orElseThrow(()-> new ResourceNotFoundException(ErrorCode.INVENTORY_STATUS_NOT_FOUND, "Default status 'DEPODA' not found in database."));

        User currentUser = getCurrentUser();

        inventoryItem.setCurrentStatus(defaultStatus);
        inventoryItem.setCreatedBy(currentUser.getId());
        inventoryItem.setLastModifiedBy(currentUser.getId());

        InventoryItem savedInventoryItem = inventoryItemRepository.save(inventoryItem);

        return inventoryItemMapper.toInventoryItemResponse(savedInventoryItem);
    }

    @Override
    @Transactional
    public InventoryItemResponse updateInventoryItem(UUID id, InventoryUpdateRequest request) {

        InventoryItem inventoryItem = findInventoryItemByIdOrThrow(id);

        inventoryItemMapper.updateInventoryItemFromRequest(request, inventoryItem);

        InventoryType inventoryType = inventoryTypeRepository
                .findById(request.getInventoryTypeId()).orElseThrow(()->new ResourceNotFoundException(ErrorCode.INVENTORY_TYPE_NOT_FOUND));

        InventoryStatus status = inventoryStatusRepository
                .findById(request.getCurrentStatusId()).orElseThrow(()-> new ResourceNotFoundException(ErrorCode.INVENTORY_STATUS_NOT_FOUND));

        User currentUser = getCurrentUser();

        inventoryItem.setInventoryType(inventoryType);
        inventoryItem.setCurrentStatus(status);
        inventoryItem.setLastModifiedBy(currentUser.getId());

        InventoryItem updatedItem = inventoryItemRepository.save(inventoryItem);

        return inventoryItemMapper.toInventoryItemResponse(updatedItem);
    }

    @Override
    @Transactional
    public void deleteInventoryItem(UUID id) {
        if (!inventoryItemRepository.existsById(id)){
            throw new ResourceNotFoundException(ErrorCode.INVENTORY_ITEM_NOT_FOUND);
        }
        inventoryItemRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<InventoryStatusHistoryResponse> getInventoryStatusHistories(UUID itemId) {
        if (!inventoryItemRepository.existsById(itemId)) {
            throw new ResourceNotFoundException(ErrorCode.INVENTORY_ITEM_NOT_FOUND);
        }

        List<InventoryStatusHistory> historyList = inventoryStatusHistoryRepository.findByInventoryItemId(itemId);

        return inventoryStatusHistoryMapper.toResponseList(historyList);
    }

    private InventoryItem findInventoryItemByIdOrThrow(UUID id){
        return inventoryItemRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException(ErrorCode.INVENTORY_ITEM_NOT_FOUND));
    }

    private User getCurrentUser(){
        return userRepository.findByUsername(SecurityUtils.getCurrentUsername())
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.USER_NOT_FOUND));
    }
}
