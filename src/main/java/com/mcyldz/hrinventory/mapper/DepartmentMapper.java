package com.mcyldz.hrinventory.mapper;

import com.mcyldz.hrinventory.dto.request.DepartmentCreateRequest;
import com.mcyldz.hrinventory.dto.request.DepartmentUpdateRequest;
import com.mcyldz.hrinventory.dto.response.DepartmentResponse;
import com.mcyldz.hrinventory.entity.Department;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DepartmentMapper {

    Department toDepartment(DepartmentCreateRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateDepartmentFromRequest(DepartmentUpdateRequest request, @MappingTarget Department department);

    DepartmentResponse toDepartmentResponse(Department department);

    List<DepartmentResponse> toDepartmentResponseList(List<Department> departmentList);
}