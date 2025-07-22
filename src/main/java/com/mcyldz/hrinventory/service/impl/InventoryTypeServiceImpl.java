package com.mcyldz.hrinventory.service.impl;

import com.mcyldz.hrinventory.dto.request.InventoryTypeCreateRequest;
import com.mcyldz.hrinventory.dto.request.InventoryTypeUpdateRequest;
import com.mcyldz.hrinventory.dto.response.InventoryTypeResponse;
import com.mcyldz.hrinventory.entity.InventoryType;
import com.mcyldz.hrinventory.exception.model.DuplicateResourceException;
import com.mcyldz.hrinventory.exception.model.ErrorCode;
import com.mcyldz.hrinventory.exception.model.ResourceNotFoundException;
import com.mcyldz.hrinventory.mapper.InventoryTypeMapper;
import com.mcyldz.hrinventory.repository.InventoryTypeRepository;
import com.mcyldz.hrinventory.service.InventoryTypeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class InventoryTypeServiceImpl implements InventoryTypeService {

    private final InventoryTypeRepository inventoryTypeRepository;
    private final InventoryTypeMapper inventoryTypeMapper;

    public InventoryTypeServiceImpl(InventoryTypeRepository inventoryTypeRepository, InventoryTypeMapper inventoryTypeMapper) {
        this.inventoryTypeRepository = inventoryTypeRepository;
        this.inventoryTypeMapper = inventoryTypeMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public InventoryTypeResponse getTypeById(UUID id) {
        return inventoryTypeMapper.toInventoryTypeResponse(findTypeByIdOrThrow(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<InventoryTypeResponse> getAllTypes() {
        return inventoryTypeMapper.toInventoryTypeResponseList(inventoryTypeRepository.findAll());
    }

    @Override
    @Transactional
    public InventoryTypeResponse createType(InventoryTypeCreateRequest request) {
        inventoryTypeRepository.findByName(request.getName()).ifPresent(inventoryType -> {throw new DuplicateResourceException(ErrorCode.INVENTORY_TYPE_NAME_ALREADY_EXISTS);
        });

        InventoryType type = inventoryTypeMapper.toInventoryType(request);
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

        inventoryTypeMapper.updateInventoryTypeFromRequest(request, existingType);
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
}
