package com.mcyldz.hrinventory.service.impl;

import com.mcyldz.hrinventory.dto.request.EducationLevelCreateRequest;
import com.mcyldz.hrinventory.dto.request.EducationLevelUpdateRequest;
import com.mcyldz.hrinventory.dto.response.EducationLevelResponse;
import com.mcyldz.hrinventory.entity.EducationLevel;
import com.mcyldz.hrinventory.entity.User;
import com.mcyldz.hrinventory.exception.model.ErrorCode;
import com.mcyldz.hrinventory.exception.model.ResourceNotFoundException;
import com.mcyldz.hrinventory.mapper.EducationLevelMapper;
import com.mcyldz.hrinventory.repository.EducationLevelRepository;
import com.mcyldz.hrinventory.repository.UserRepository;
import com.mcyldz.hrinventory.service.EducationLevelService;
import com.mcyldz.hrinventory.util.SecurityUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class EducationLevelServiceImpl implements EducationLevelService {

    private final EducationLevelRepository educationLevelRepository;
    private final EducationLevelMapper educationLevelMapper;
    private final UserRepository userRepository;

    public EducationLevelServiceImpl(EducationLevelRepository educationLevelRepository, EducationLevelMapper educationLevelMapper, UserRepository userRepository) {
        this.educationLevelRepository = educationLevelRepository;
        this.educationLevelMapper = educationLevelMapper;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public EducationLevelResponse getEducationLevelById(UUID id) {

        return educationLevelMapper.toEducationLevelResponse(findEducationLevelByIdOrThrow(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<EducationLevelResponse> getAllEducationLevels(Integer page, Integer size) {

        Pageable pageable = PageRequest.of(page, size);

        Page<EducationLevel> educationLevelPage = educationLevelRepository.findAll(pageable);

        List<EducationLevel> educationLevelList = educationLevelPage.getContent();

        return educationLevelMapper.toEducationLevelResponseList(educationLevelList);
    }

    @Override
    @Transactional
    public EducationLevelResponse createEducationLevel(EducationLevelCreateRequest request) {

        User currentUser = getCurrentUser();

        EducationLevel educationLevel = educationLevelMapper.toEducationLevel(request);
        educationLevel.setCreatedBy(currentUser.getId());
        educationLevel.setLastModifiedBy(currentUser.getId());
        EducationLevel savedEducationLevel = educationLevelRepository.save(educationLevel);

        return educationLevelMapper.toEducationLevelResponse(savedEducationLevel);
    }

    @Override
    @Transactional
    public EducationLevelResponse updateEducationLevel(UUID id, EducationLevelUpdateRequest request) {

        User currentUser = getCurrentUser();

        EducationLevel existingEducationLevel = findEducationLevelByIdOrThrow(id);
        educationLevelMapper.updateEducationLevelFromRequest(request, existingEducationLevel);
        existingEducationLevel.setLastModifiedBy(currentUser.getId());
        EducationLevel updatedEducationLevel = educationLevelRepository.save(existingEducationLevel);

        return educationLevelMapper.toEducationLevelResponse(updatedEducationLevel);
    }

    @Override
    @Transactional
    public void deleteEducationLevel(UUID id) {

        if (!educationLevelRepository.existsById(id)) {
            throw new ResourceNotFoundException(ErrorCode.EDUCATION_LEVEL_NOT_FOUND);
        }

        educationLevelRepository.deleteById(id);
    }

    private EducationLevel findEducationLevelByIdOrThrow(UUID id) {

        return educationLevelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.EDUCATION_LEVEL_NOT_FOUND));
    }

    private User getCurrentUser(){
        return userRepository.findByUsername(SecurityUtils.getCurrentUsername())
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.USER_NOT_FOUND));
    }
}
