package com.mcyldz.hrinventory.service.impl;

import com.mcyldz.hrinventory.dto.request.DepartmentCreateRequest;
import com.mcyldz.hrinventory.dto.request.DepartmentUpdateRequest;
import com.mcyldz.hrinventory.dto.response.DepartmentResponse;
import com.mcyldz.hrinventory.entity.Department;
import com.mcyldz.hrinventory.entity.User;
import com.mcyldz.hrinventory.exception.model.ErrorCode;
import com.mcyldz.hrinventory.exception.model.ResourceNotFoundException;
import com.mcyldz.hrinventory.mapper.DepartmentMapper;
import com.mcyldz.hrinventory.repository.DepartmentRepository;
import com.mcyldz.hrinventory.repository.UserRepository;
import com.mcyldz.hrinventory.service.DepartmentService;
import com.mcyldz.hrinventory.util.SecurityUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final DepartmentMapper departmentMapper;
    private final UserRepository userRepository;

    public DepartmentServiceImpl(DepartmentRepository departmentRepository, DepartmentMapper departmentMapper, UserRepository userRepository) {
        this.departmentRepository = departmentRepository;
        this.departmentMapper = departmentMapper;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public DepartmentResponse getDepartmentById(UUID id) {

        return departmentMapper.toDepartmentResponse(findDepartmentByIdOrThrow(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<DepartmentResponse> getAllDepartments(Integer page, Integer size) {

        Pageable pageable = PageRequest.of(page, size);

        Page<Department> departmentPage = departmentRepository.findAll(pageable);

        List<Department> departmentList = departmentPage.getContent();

        return departmentMapper.toDepartmentResponseList(departmentList);
    }

    @Override
    @Transactional
    public DepartmentResponse createDepartment(DepartmentCreateRequest request) {

        User currentUser = getCurrentUser();

        Department department = departmentMapper.toDepartment(request);
        department.setCreatedBy(currentUser.getId());
        department.setLastModifiedBy(currentUser.getId());
        Department savedDepartment = departmentRepository.save(department);

        return departmentMapper.toDepartmentResponse(savedDepartment);
    }

    @Override
    @Transactional
    public DepartmentResponse updateDepartment(UUID id, DepartmentUpdateRequest request) {

        User currentUser = getCurrentUser();

        Department existingDepartment = findDepartmentByIdOrThrow(id);
        departmentMapper.updateDepartmentFromRequest(request, existingDepartment);
        existingDepartment.setLastModifiedBy(currentUser.getId());
        Department updatedDepartment = departmentRepository.save(existingDepartment);

        return departmentMapper.toDepartmentResponse(updatedDepartment);
    }

    @Override
    @Transactional
    public void deleteDepartment(UUID id) {

        if (!departmentRepository.existsById(id)) {
            throw new ResourceNotFoundException(ErrorCode.DEPARTMENT_NOT_FOUND);
        }
        departmentRepository.deleteById(id);

    }

    private Department findDepartmentByIdOrThrow(UUID id) {
        return departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.DEPARTMENT_NOT_FOUND));
    }

    private User getCurrentUser(){
        return userRepository.findByUsername(SecurityUtils.getCurrentUsername())
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.USER_NOT_FOUND));
    }
}
