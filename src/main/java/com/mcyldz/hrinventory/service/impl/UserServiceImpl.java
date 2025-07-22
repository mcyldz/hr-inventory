package com.mcyldz.hrinventory.service.impl;

import com.mcyldz.hrinventory.dto.request.UserCreateRequest;
import com.mcyldz.hrinventory.dto.request.UserUpdateRequest;
import com.mcyldz.hrinventory.dto.response.UserResponse;
import com.mcyldz.hrinventory.entity.Personnel;
import com.mcyldz.hrinventory.entity.Role;
import com.mcyldz.hrinventory.entity.User;
import com.mcyldz.hrinventory.exception.model.DuplicateResourceException;
import com.mcyldz.hrinventory.exception.model.ErrorCode;
import com.mcyldz.hrinventory.exception.model.ResourceNotFoundException;
import com.mcyldz.hrinventory.mapper.UserMapper;
import com.mcyldz.hrinventory.repository.PersonnelRepository;
import com.mcyldz.hrinventory.repository.RoleRepository;
import com.mcyldz.hrinventory.repository.UserRepository;
import com.mcyldz.hrinventory.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PersonnelRepository personnelRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PersonnelRepository personnelRepository, UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.personnelRepository = personnelRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponse> getAllUsers() {
        return userMapper.toUserResponseList(userRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getUserById(UUID id) {
        return userMapper.toUserResponse(findUserByIdOrThrow(id));
    }

    @Override
    @Transactional
    public UserResponse createUser(UserCreateRequest request) {
        userRepository.findByUsername(request.getUsername()).ifPresent(user -> {
            throw new DuplicateResourceException(ErrorCode.USERNAME_ALREADY_EXISTS);
        });

        User user = userMapper.toUser(request);

        user.setPassword(passwordEncoder.encode(request.getPassword()));

        Role role = roleRepository.findById(request.getRoleId())
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.ROLE_NOT_FOUND));

        user.setRole(role);

        if (request.getPersonnelId() != null) {
            Personnel personnel = personnelRepository.findById(request.getPersonnelId())
                    .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.PERSONNEL_NOT_FOUND));
            user.setPersonnel(personnel);
        }

        User savedUser = userRepository.save(user);

        return userMapper.toUserResponse(savedUser);
    }

    @Override
    @Transactional
    public UserResponse updateUser(UUID id, UserUpdateRequest request) {

        User existingUser = findUserByIdOrThrow(id);

        userMapper.updateUserFromRequest(request, existingUser);

        Role role = roleRepository.findById(request.getRoleId())
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.ROLE_NOT_FOUND));

        existingUser.setRole(role);

        User updatedUser = userRepository.save(existingUser);

        return userMapper.toUserResponse(updatedUser);
    }

    @Override
    @Transactional
    public void deleteUser(UUID id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException(ErrorCode.USER_NOT_FOUND);
        }
        userRepository.deleteById(id);
    }

    private User findUserByIdOrThrow(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.USER_NOT_FOUND));
    }
}
