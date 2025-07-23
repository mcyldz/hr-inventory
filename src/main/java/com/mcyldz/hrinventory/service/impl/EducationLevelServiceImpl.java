package com.mcyldz.hrinventory.service.impl;

import com.mcyldz.hrinventory.dto.request.EducationLevelCreateRequest;
import com.mcyldz.hrinventory.dto.request.EducationLevelUpdateRequest;
import com.mcyldz.hrinventory.dto.response.EducationLevelResponse;
import com.mcyldz.hrinventory.entity.EducationLevel;
import com.mcyldz.hrinventory.exception.model.ErrorCode;
import com.mcyldz.hrinventory.exception.model.ResourceNotFoundException;
import com.mcyldz.hrinventory.mapper.EducationLevelMapper;
import com.mcyldz.hrinventory.repository.EducationLevelRepository;
import com.mcyldz.hrinventory.service.EducationLevelService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class EducationLevelServiceImpl implements EducationLevelService {

    private final EducationLevelRepository educationLevelRepository;
    private final EducationLevelMapper educationLevelMapper;

    public EducationLevelServiceImpl(EducationLevelRepository educationLevelRepository, EducationLevelMapper educationLevelMapper) {
        this.educationLevelRepository = educationLevelRepository;
        this.educationLevelMapper = educationLevelMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public EducationLevelResponse getEducationLevelById(UUID id) {

        return educationLevelMapper.toEducationLevelResponse(findEducationLevelByIdOrThrow(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<EducationLevelResponse> getAllEducationLevels() {

        return educationLevelMapper.toEducationLevelResponseList(educationLevelRepository.findAll());
    }

    @Override
    @Transactional
    public EducationLevelResponse createEducationLevel(EducationLevelCreateRequest request) {

        EducationLevel educationLevel = educationLevelMapper.toEducationLevel(request);
        EducationLevel savedEducationLevel = educationLevelRepository.save(educationLevel);

        return educationLevelMapper.toEducationLevelResponse(savedEducationLevel);
    }

    @Override
    @Transactional
    public EducationLevelResponse updateEducationLevel(UUID id, EducationLevelUpdateRequest request) {

        EducationLevel existingEducationLevel = findEducationLevelByIdOrThrow(id);
        educationLevelMapper.updateEducationLevelFromRequest(request, existingEducationLevel);
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
}
