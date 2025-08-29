package com.mcyldz.hrinventory.service.impl;

import com.mcyldz.hrinventory.dto.request.InventoryTypeCreateRequest;
import com.mcyldz.hrinventory.dto.request.InventoryTypeUpdateRequest;
import com.mcyldz.hrinventory.dto.response.InventoryTypeResponse;
import com.mcyldz.hrinventory.entity.InventoryType;
import com.mcyldz.hrinventory.entity.User;
import com.mcyldz.hrinventory.exception.model.DuplicateResourceException;
import com.mcyldz.hrinventory.exception.model.ErrorCode;
import com.mcyldz.hrinventory.exception.model.ResourceNotFoundException;
import com.mcyldz.hrinventory.mapper.InventoryTypeMapper;
import com.mcyldz.hrinventory.repository.InventoryTypeRepository;
import com.mcyldz.hrinventory.repository.UserRepository;
import com.mcyldz.hrinventory.service.InventoryTypeService;
import com.mcyldz.hrinventory.util.SecurityUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class InventoryTypeServiceImpl implements InventoryTypeService {

    private final InventoryTypeRepository inventoryTypeRepository;
    private final InventoryTypeMapper inventoryTypeMapper;
    private final UserRepository userRepository;

    public InventoryTypeServiceImpl(InventoryTypeRepository inventoryTypeRepository, InventoryTypeMapper inventoryTypeMapper, UserRepository userRepository) {
        this.inventoryTypeRepository = inventoryTypeRepository;
        this.inventoryTypeMapper = inventoryTypeMapper;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public InventoryTypeResponse getTypeById(UUID id) {
        return inventoryTypeMapper.toInventoryTypeResponse(findTypeByIdOrThrow(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<InventoryTypeResponse> getAllTypes(Integer page, Integer size) {

        Pageable pageable = PageRequest.of(page, size);

        Page<InventoryType> inventoryTypePage = inventoryTypeRepository.findAll(pageable);

        List<InventoryType> inventoryTypeList = inventoryTypePage.getContent();

        return inventoryTypeMapper.toInventoryTypeResponseList(inventoryTypeList);
    }

    @Override
    @Transactional
    public InventoryTypeResponse createType(InventoryTypeCreateRequest request) {

        inventoryTypeRepository.findByName(request.getName()).ifPresent(inventoryType -> {throw new DuplicateResourceException(ErrorCode.INVENTORY_TYPE_NAME_ALREADY_EXISTS);
        });

        User currentUser = getCurrentUser();

        InventoryType type = inventoryTypeMapper.toInventoryType(request);
        type.setCreatedBy(currentUser.getId());
        type.setLastModifiedBy(currentUser.getId());
        InventoryType savedType = inventoryTypeRepository.save(type);

        return inventoryTypeMapper.toInventoryTypeResponse(savedType);
    }

    @Override
    @Transactional
    public InventoryTypeResponse updateType(UUID id, InventoryTypeUpdateRequest request) {

        InventoryType existingType = findTypeByIdOrThrow(id);

        inventoryTypeRepository.findByName(request.getName()).ifPresent(foundType -> {
            if (!foundType.getId().equals(id)) {
                throw new DuplicateResourceException(ErrorCode.INVENTORY_TYPE_NAME_ALREADY_EXISTS);
            }
        });

        User currentUser = getCurrentUser();

        inventoryTypeMapper.updateInventoryTypeFromRequest(request, existingType);

        existingType.setLastModifiedBy(currentUser.getId());
        InventoryType updatedType = inventoryTypeRepository.save(existingType);

        return inventoryTypeMapper.toInventoryTypeResponse(updatedType);
    }

    @Override
    @Transactional
    public void deleteType(UUID id) {
        if (!inventoryTypeRepository.existsById(id)) {
            throw new ResourceNotFoundException(ErrorCode.INVENTORY_TYPE_NOT_FOUND);
        }
        inventoryTypeRepository.deleteById(id);
    }

    private InventoryType findTypeByIdOrThrow(UUID id) {
        return inventoryTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.INVENTORY_TYPE_NOT_FOUND));
    }

    private User getCurrentUser(){
        return userRepository.findByUsername(SecurityUtils.getCurrentUsername())
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.USER_NOT_FOUND));
    }
}
