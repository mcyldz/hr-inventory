package com.mcyldz.hrinventory.service.impl;

import com.mcyldz.hrinventory.dto.request.InventoryStatusCreateRequest;
import com.mcyldz.hrinventory.dto.request.InventoryStatusUpdateRequest;
import com.mcyldz.hrinventory.dto.response.InventoryStatusResponse;
import com.mcyldz.hrinventory.entity.InventoryStatus;
import com.mcyldz.hrinventory.entity.User;
import com.mcyldz.hrinventory.exception.model.DuplicateResourceException;
import com.mcyldz.hrinventory.exception.model.ErrorCode;
import com.mcyldz.hrinventory.exception.model.ResourceNotFoundException;
import com.mcyldz.hrinventory.mapper.InventoryStatusMapper;
import com.mcyldz.hrinventory.repository.InventoryStatusRepository;
import com.mcyldz.hrinventory.repository.UserRepository;
import com.mcyldz.hrinventory.service.InventoryStatusService;
import com.mcyldz.hrinventory.util.SecurityUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class InventoryStatusServiceImpl implements InventoryStatusService {

    private final InventoryStatusRepository inventoryStatusRepository;

    private final InventoryStatusMapper inventoryStatusMapper;

    private final UserRepository userRepository;

    public InventoryStatusServiceImpl(InventoryStatusRepository inventoryStatusRepository, InventoryStatusMapper inventoryStatusMapper, UserRepository userRepository) {
        this.inventoryStatusRepository = inventoryStatusRepository;
        this.inventoryStatusMapper = inventoryStatusMapper;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public InventoryStatusResponse getStatusById(UUID id) {
        return inventoryStatusMapper.toInventoryStatusResponse(findStatusByIdOrThrow(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<InventoryStatusResponse> getAllStatuses(Integer page, Integer size) {

        Pageable pageable = PageRequest.of(page, size);

        Page<InventoryStatus> inventoryStatusPage = inventoryStatusRepository.findAll(pageable);

        List<InventoryStatus> inventoryStatusList = inventoryStatusPage.getContent();

        return inventoryStatusMapper.toInventoryStatusResponseList(inventoryStatusList);
    }

    @Override
    @Transactional
    public InventoryStatusResponse createStatus(InventoryStatusCreateRequest request) {

        inventoryStatusRepository.findByName(request.getName()).ifPresent(s -> {
            throw new DuplicateResourceException(ErrorCode.INVENTORY_STATUS_NAME_ALREADY_EXISTS);
        });

        User currentUser = getCurrentUser();

        InventoryStatus status = inventoryStatusMapper.toInventoryStatus(request);

        status.setCreatedBy(currentUser.getId());
        status.setLastModifiedBy(currentUser.getId());

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

        User currentUser = getCurrentUser();

        inventoryStatusMapper.updateInventoryStatusFromRequest(request, existingStatus);

        existingStatus.setLastModifiedBy(currentUser.getId());
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

    private User getCurrentUser(){
        return userRepository.findByUsername(SecurityUtils.getCurrentUsername())
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.USER_NOT_FOUND));
    }
}
