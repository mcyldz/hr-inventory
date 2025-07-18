package com.mcyldz.hrinventory.mapper;

import com.mcyldz.hrinventory.dto.request.InventoryTypeCreateRequest;
import com.mcyldz.hrinventory.dto.request.InventoryTypeUpdateRequest;
import com.mcyldz.hrinventory.dto.response.InventoryTypeResponse;
import com.mcyldz.hrinventory.entity.InventoryType;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring")
public interface InventoryTypeMapper {

    InventoryType toInventoryType(InventoryTypeCreateRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateInventoryTypeFromRequest(InventoryTypeUpdateRequest request, @MappingTarget InventoryType inventoryType);

    InventoryTypeResponse toInventoryTypeResponse(InventoryType inventoryType);

    List<InventoryTypeResponse> toInventoryTypeResponseList(List<InventoryType> inventoryTypeList);
}
