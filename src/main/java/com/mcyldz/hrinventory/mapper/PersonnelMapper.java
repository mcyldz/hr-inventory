package com.mcyldz.hrinventory.mapper;

import com.mcyldz.hrinventory.dto.request.PersonnelCreateRequest;
import com.mcyldz.hrinventory.dto.request.PersonnelUpdateRequest;
import com.mcyldz.hrinventory.dto.response.PersonnelResponse;
import com.mcyldz.hrinventory.entity.Personnel;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PersonnelMapper {

    @Mapping(target = "profilePhoto", ignore = true)
    @Mapping(target = "educationLevel", ignore = true)
    @Mapping(target = "department", ignore = true)
    @Mapping(target = "position", ignore = true)
    @Mapping(target = "isActive", constant = "true")
    Personnel toPersonnel(PersonnelCreateRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "educationLevel", ignore = true)
    @Mapping(target = "department", ignore = true)
    @Mapping(target = "position", ignore = true)
    void updatePersonnelFromRequest(PersonnelUpdateRequest request, @MappingTarget Personnel personnel);

    @Mapping(source = "educationLevel.name", target = "educationLevelName")
    @Mapping(source = "department.name", target = "departmentName")
    @Mapping(source = "position.name", target = "positionName")
    PersonnelResponse toPersonnelResponse(Personnel personnel);

    List<PersonnelResponse> toPersonnelResponseList(List<Personnel> personnelList);
}
