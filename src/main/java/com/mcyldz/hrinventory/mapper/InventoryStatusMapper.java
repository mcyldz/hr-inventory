package com.mcyldz.hrinventory.mapper;

import com.mcyldz.hrinventory.dto.request.InventoryStatusCreateRequest;
import com.mcyldz.hrinventory.dto.request.InventoryStatusUpdateRequest;
import com.mcyldz.hrinventory.dto.response.InventoryStatusResponse;
import com.mcyldz.hrinventory.entity.InventoryStatus;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring")
public interface InventoryStatusMapper {

    InventoryStatus toInventoryStatus(InventoryStatusCreateRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateInventoryStatusFromRequest(InventoryStatusUpdateRequest request, @MappingTarget InventoryStatus inventoryStatus);

    InventoryStatusResponse toInventoryStatusResponse(InventoryStatus inventoryStatus);

    List<InventoryStatusResponse> toInventoryStatusResponseList(List<InventoryStatus> inventoryStatusList);
}
