package com.mcyldz.hrinventory.mapper;

import com.mcyldz.hrinventory.dto.request.RoleCreateRequest;
import com.mcyldz.hrinventory.dto.request.RoleUpdateRequest;
import com.mcyldz.hrinventory.dto.response.RoleResponse;
import com.mcyldz.hrinventory.entity.Role;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    Role toRole(RoleCreateRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateRoleFromRequest(RoleUpdateRequest request, @MappingTarget Role role);

    RoleResponse toRoleResponse(Role role);

    List<RoleResponse> toRoleResponseList(List<Role> roleList);
}
