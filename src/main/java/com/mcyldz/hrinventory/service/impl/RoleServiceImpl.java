package com.mcyldz.hrinventory.service.impl;

import com.mcyldz.hrinventory.dto.request.RoleCreateRequest;
import com.mcyldz.hrinventory.dto.request.RoleUpdateRequest;
import com.mcyldz.hrinventory.dto.response.RoleResponse;
import com.mcyldz.hrinventory.entity.Role;
import com.mcyldz.hrinventory.exception.model.DuplicateResourceException;
import com.mcyldz.hrinventory.exception.model.ErrorCode;
import com.mcyldz.hrinventory.exception.model.ResourceNotFoundException;
import com.mcyldz.hrinventory.mapper.RoleMapper;
import com.mcyldz.hrinventory.repository.RoleRepository;
import com.mcyldz.hrinventory.service.RoleService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    private final RoleMapper roleMapper;

    public RoleServiceImpl(RoleRepository roleRepository, RoleMapper roleMapper) {
        this.roleRepository = roleRepository;
        this.roleMapper = roleMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public RoleResponse getRoleById(UUID id) {

        Role role = findRoleByIdOrThrow(id);
        return roleMapper.toRoleResponse(role);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoleResponse> getAllRoles(Integer page, Integer size) {

        Pageable pageable = PageRequest.of(page, size);

        Page<Role> rolePage = roleRepository.findAll(pageable);

        List<Role> roleList = rolePage.getContent();

        return roleMapper.toRoleResponseList(roleList);
    }

    @Override
    @Transactional
    public RoleResponse createRole(RoleCreateRequest request) {

        roleRepository.findByName(request.getName())
                .ifPresent(role -> {throw new DuplicateResourceException(ErrorCode.ROLE_NAME_ALREADY_EXISTS);});

        Role role = roleMapper.toRole(request);
        Role savedRole = roleRepository.save(role);
        return roleMapper.toRoleResponse(savedRole);
    }

    @Override
    @Transactional
    public RoleResponse updateRole(UUID id, RoleUpdateRequest request) {

        Role existingRole = findRoleByIdOrThrow(id);
        roleMapper.updateRoleFromRequest(request, existingRole);
        Role updatedRole = roleRepository.save(existingRole);
        return roleMapper.toRoleResponse(updatedRole);
    }

    @Override
    @Transactional
    public void deleteRole(UUID id) {

        if (!roleRepository.existsById(id)){
            throw new ResourceNotFoundException(ErrorCode.RESOURCE_NOT_FOUND);
        }
        roleRepository.deleteById(id);
    }

    private Role findRoleByIdOrThrow(UUID id){
        return roleRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException(ErrorCode.ROLE_NOT_FOUND));
    }
}
