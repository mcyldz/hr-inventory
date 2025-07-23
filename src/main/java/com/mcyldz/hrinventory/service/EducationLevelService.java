package com.mcyldz.hrinventory.service;

import com.mcyldz.hrinventory.dto.request.EducationLevelCreateRequest;
import com.mcyldz.hrinventory.dto.request.EducationLevelUpdateRequest;
import com.mcyldz.hrinventory.dto.response.EducationLevelResponse;

import java.util.List;
import java.util.UUID;

public interface EducationLevelService {

    EducationLevelResponse getEducationLevelById(UUID id);

    List<EducationLevelResponse> getAllEducationLevels();

    EducationLevelResponse createEducationLevel(EducationLevelCreateRequest request);

    EducationLevelResponse updateEducationLevel(UUID id, EducationLevelUpdateRequest request);

    void deleteEducationLevel(UUID id);
}
