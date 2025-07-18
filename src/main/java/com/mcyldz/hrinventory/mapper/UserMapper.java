package com.mcyldz.hrinventory.mapper;

import com.mcyldz.hrinventory.dto.request.UserCreateRequest;
import com.mcyldz.hrinventory.dto.request.UserUpdateRequest;
import com.mcyldz.hrinventory.dto.response.UserResponse;
import com.mcyldz.hrinventory.entity.User;
import org.mapstruct.*;

import java.util.List;

@Mapper(
        componentModel = "spring",
        uses = {HelperMapper.class}
)
public interface UserMapper {

    @Mapping(target = "password", ignore = true)
    @Mapping(target = "personnel", ignore = true)
    @Mapping(target = "role", ignore = true)
    User toUser(UserCreateRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "personnel", ignore = true)
    void updateUserFromRequest(UserUpdateRequest request, @MappingTarget User user);

    @Mapping(source = "role.name", target = "roleName")
    @Mapping(source = "personnel", target = "personnelFullName")
    UserResponse toUserResponse(User user);

    List<UserResponse> toUserResponseList(List<User> userList);
}
