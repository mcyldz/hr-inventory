package com.mcyldz.hrinventory.service;

import com.mcyldz.hrinventory.dto.request.RoleCreateRequest;
import com.mcyldz.hrinventory.dto.request.RoleUpdateRequest;
import com.mcyldz.hrinventory.dto.response.RoleResponse;

import java.util.List;
import java.util.UUID;

public interface RoleService {

    RoleResponse getRoleById(UUID id);

    List<RoleResponse> getAllRoles();

    RoleResponse createRole(RoleCreateRequest request);

    RoleResponse updateRole(UUID id, RoleUpdateRequest request);

    void deleteRole(UUID id);
}
