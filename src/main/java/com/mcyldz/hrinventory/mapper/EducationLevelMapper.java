package com.mcyldz.hrinventory.mapper;

import com.mcyldz.hrinventory.dto.request.EducationLevelCreateRequest;
import com.mcyldz.hrinventory.dto.request.EducationLevelUpdateRequest;
import com.mcyldz.hrinventory.dto.response.EducationLevelResponse;
import com.mcyldz.hrinventory.entity.EducationLevel;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EducationLevelMapper {

    EducationLevel toEducationLevel(EducationLevelCreateRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEducationLevelFromRequest(EducationLevelUpdateRequest request, @MappingTarget EducationLevel educationLevel);

    EducationLevelResponse toEducationLevelResponse(EducationLevel educationLevel);

    List<EducationLevelResponse> toEducationLevelResponseList(List<EducationLevel> educationLevelList);
}
