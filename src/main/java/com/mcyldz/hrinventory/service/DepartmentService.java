package com.mcyldz.hrinventory.service;

import com.mcyldz.hrinventory.dto.request.DepartmentCreateRequest;
import com.mcyldz.hrinventory.dto.request.DepartmentUpdateRequest;
import com.mcyldz.hrinventory.dto.response.DepartmentResponse;

import java.util.List;
import java.util.UUID;

public interface DepartmentService {

    DepartmentResponse getDepartmentById(UUID id);

    List<DepartmentResponse> getAllDepartments();

    DepartmentResponse createDepartment(DepartmentCreateRequest request);

    DepartmentResponse updateDepartment(UUID id, DepartmentUpdateRequest request);

    void deleteDepartment(UUID id);
}
