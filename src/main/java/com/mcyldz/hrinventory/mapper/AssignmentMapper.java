package com.mcyldz.hrinventory.mapper;

import com.mcyldz.hrinventory.dto.response.AssignmentResponse;
import com.mcyldz.hrinventory.entity.PersonnelInventoryAssignment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(
        componentModel = "spring",
        uses = {HelperMapper.class}
)
public interface AssignmentMapper {

    @Mapping(source = "personnel.id", target = "personnelId")
    @Mapping(source = "personnel", target = "personnelName")
    @Mapping(source = "inventoryItem.id", target = "inventoryItemId")
    @Mapping(source = "inventoryItem", target = "inventoryItemName")
    @Mapping(source = "assignedBy.username", target = "assignedByUser")
    @Mapping(source = "returnedBy.username", target = "returnedByUser")
    AssignmentResponse toAssignmentResponse(PersonnelInventoryAssignment assignment);

    List<AssignmentResponse> toAssignmentResponseList(List<PersonnelInventoryAssignment> assignmentList);
}
