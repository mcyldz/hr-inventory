package com.mcyldz.hrinventory.service.impl;

import com.mcyldz.hrinventory.dto.request.InventoryStatusCreateRequest;
import com.mcyldz.hrinventory.dto.request.InventoryStatusUpdateRequest;
import com.mcyldz.hrinventory.dto.response.InventoryStatusResponse;
import com.mcyldz.hrinventory.entity.InventoryStatus;
import com.mcyldz.hrinventory.exception.model.DuplicateResourceException;
import com.mcyldz.hrinventory.exception.model.ErrorCode;
import com.mcyldz.hrinventory.exception.model.ResourceNotFoundException;
import com.mcyldz.hrinventory.mapper.InventoryStatusMapper;
import com.mcyldz.hrinventory.repository.InventoryStatusRepository;
import com.mcyldz.hrinventory.service.InventoryStatusService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class InventoryStatusServiceImpl implements InventoryStatusService {

    private final InventoryStatusRepository inventoryStatusRepository;

    private final InventoryStatusMapper inventoryStatusMapper;

    public InventoryStatusServiceImpl(InventoryStatusRepository inventoryStatusRepository, InventoryStatusMapper inventoryStatusMapper) {
        this.inventoryStatusRepository = inventoryStatusRepository;
        this.inventoryStatusMapper = inventoryStatusMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public InventoryStatusResponse getStatusById(UUID id) {
        return inventoryStatusMapper.toInventoryStatusResponse(findStatusByIdOrThrow(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<InventoryStatusResponse> getAllStatuses() {
        return inventoryStatusMapper.toInventoryStatusResponseList(inventoryStatusRepository.findAll());
    }

    @Override
    @Transactional
    public InventoryStatusResponse createStatus(InventoryStatusCreateRequest request) {

        inventoryStatusRepository.findByName(request.getName()).ifPresent(s -> {
            throw new DuplicateResourceException(ErrorCode.INVENTORY_STATUS_NAME_ALREADY_EXISTS);
        });

        InventoryStatus status = inventoryStatusMapper.toInventoryStatus(request);
        InventoryStatus savedStatus = inventoryStatusRepository.save(status);
        return inventoryStatusMapper.toInventoryStatusResponse(savedStatus);
    }

    @Override
    @Transactional
    public InventoryStatusResponse updateStatus(UUID id, InventoryStatusUpdateRequest request) {
        InventoryStatus existingStatus = findStatusByIdOrThrow(id);

        inventoryStatusRepository.findByName(request.getName()).ifPresent(foundStatus -> {
            if (!foundStatus.getId().equals(id)) {
                throw new DuplicateResourceException(ErrorCode.INVENTORY_STATUS_NAME_ALREADY_EXISTS);
            }
        });

        inventoryStatusMapper.updateInventoryStatusFromRequest(request, existingStatus);
        InventoryStatus updatedStatus = inventoryStatusRepository.save(existingStatus);
        return inventoryStatusMapper.toInventoryStatusResponse(updatedStatus);
    }

    @Override
    @Transactional
    public void deleteStatus(UUID id) {
        if (!inventoryStatusRepository.existsById(id)) {
            throw new ResourceNotFoundException(ErrorCode.INVENTORY_STATUS_NOT_FOUND);
        }
        inventoryStatusRepository.deleteById(id);
    }

    private InventoryStatus findStatusByIdOrThrow(UUID id) {
        return inventoryStatusRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.INVENTORY_STATUS_NOT_FOUND));
    }
}
