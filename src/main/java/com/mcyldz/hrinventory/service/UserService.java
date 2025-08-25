package com.mcyldz.hrinventory.service;

import com.mcyldz.hrinventory.dto.request.UserCreateRequest;
import com.mcyldz.hrinventory.dto.request.UserUpdateRequest;
import com.mcyldz.hrinventory.dto.response.UserResponse;

import java.util.List;
import java.util.UUID;

public interface UserService {

    UserResponse getUserById(UUID id);

    List<UserResponse> getAllUsers(Integer page, Integer size);

    UserResponse createUser(UserCreateRequest request);

    UserResponse updateUser(UUID id, UserUpdateRequest request);

    void deleteUser(UUID id);
}
