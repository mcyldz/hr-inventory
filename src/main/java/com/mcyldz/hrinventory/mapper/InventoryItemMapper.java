package com.mcyldz.hrinventory.mapper;

import com.mcyldz.hrinventory.dto.request.InventoryCreateRequest;
import com.mcyldz.hrinventory.dto.request.InventoryUpdateRequest;
import com.mcyldz.hrinventory.dto.response.InventoryItemResponse;
import com.mcyldz.hrinventory.entity.InventoryItem;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface InventoryItemMapper {

    @Mapping(target = "inventoryType", ignore = true)
    @Mapping(target = "currentStatus", ignore = true)
    InventoryItem toInventoryItem(InventoryCreateRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "inventoryType", ignore = true)
    @Mapping(target = "currentStatus", ignore = true)
    void updateInventoryItemFromRequest(InventoryUpdateRequest request, @MappingTarget InventoryItem inventoryItem);

    @Mapping(source = "inventoryType.name", target = "inventoryTypeName")
    @Mapping(source = "currentStatus.name", target = "currentStatusName")
    InventoryItemResponse toInventoryItemResponse(InventoryItem inventoryItem);

    List<InventoryItemResponse> toInventoryItemResponseList(List<InventoryItem> inventoryItemList);
}
