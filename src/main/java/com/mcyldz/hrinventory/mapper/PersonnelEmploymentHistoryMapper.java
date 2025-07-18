package com.mcyldz.hrinventory.mapper;

import com.mcyldz.hrinventory.dto.response.PersonnelEmploymentHistoryResponse;
import com.mcyldz.hrinventory.entity.PersonnelEmploymentHistory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(
        componentModel = "spring",
        uses = {HelperMapper.class}
)
public interface PersonnelEmploymentHistoryMapper {

    @Mapping(source = "department.name", target = "departmentName")
    @Mapping(source = "position.name", target = "positionName")
    @Mapping(source = "personnel.id", target = "personnelId")
    @Mapping(source = "personnel", target = "personnelFullName")
    PersonnelEmploymentHistoryResponse toResponse(PersonnelEmploymentHistory history);

    List<PersonnelEmploymentHistoryResponse> toResponseList(List<PersonnelEmploymentHistory> historyList);
}
