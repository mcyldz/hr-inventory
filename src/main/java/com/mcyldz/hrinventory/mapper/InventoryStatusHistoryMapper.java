package com.mcyldz.hrinventory.mapper;

import com.mcyldz.hrinventory.dto.response.InventoryStatusHistoryResponse;
import com.mcyldz.hrinventory.entity.InventoryStatusHistory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(
        componentModel = "spring",
        uses = {HelperMapper.class}
)
public interface InventoryStatusHistoryMapper {
    
    @Mapping(source = "status.name", target = "statusName")
    @Mapping(source = "changedBy.username", target = "changedByUsername")
    @Mapping(source = "inventoryItem.id", target = "inventoryItemId")
    @Mapping(source = "inventoryItem", target = "inventoryItemName")
    InventoryStatusHistoryResponse toResponse(InventoryStatusHistory history);

    List<InventoryStatusHistoryResponse> toResponseList(List<InventoryStatusHistory> historyList);
}

