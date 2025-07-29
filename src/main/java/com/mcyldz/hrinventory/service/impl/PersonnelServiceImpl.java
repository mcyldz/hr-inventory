package com.mcyldz.hrinventory.service.impl;

import com.mcyldz.hrinventory.dto.request.PersonnelCreateRequest;
import com.mcyldz.hrinventory.dto.request.PersonnelUpdateRequest;
import com.mcyldz.hrinventory.dto.response.PersonnelEmploymentHistoryResponse;
import com.mcyldz.hrinventory.dto.response.PersonnelResponse;
import com.mcyldz.hrinventory.entity.*;
import com.mcyldz.hrinventory.exception.model.DuplicateResourceException;
import com.mcyldz.hrinventory.exception.model.ErrorCode;
import com.mcyldz.hrinventory.exception.model.ResourceNotFoundException;
import com.mcyldz.hrinventory.mapper.PersonnelEmploymentHistoryMapper;
import com.mcyldz.hrinventory.mapper.PersonnelMapper;
import com.mcyldz.hrinventory.repository.*;
import com.mcyldz.hrinventory.service.PersonnelService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class PersonnelServiceImpl implements PersonnelService {

    private final PersonnelRepository personnelRepository;

    private final EducationLevelRepository educationLevelRepository;

    private final DepartmentRepository departmentRepository;

    private final PositionRepository positionRepository;

    private final PersonnelEmploymentHistoryRepository historyRepository;

    private final PersonnelMapper personnelMapper;

    private final PersonnelEmploymentHistoryMapper historyMapper;

    public PersonnelServiceImpl(PersonnelRepository personnelRepository, EducationLevelRepository educationLevelRepository, DepartmentRepository departmentRepository, PositionRepository positionRepository, PersonnelEmploymentHistoryRepository historyRepository, PersonnelMapper personnelMapper, PersonnelEmploymentHistoryMapper historyMapper) {
        this.personnelRepository = personnelRepository;
        this.educationLevelRepository = educationLevelRepository;
        this.departmentRepository = departmentRepository;
        this.positionRepository = positionRepository;
        this.historyRepository = historyRepository;
        this.personnelMapper = personnelMapper;
        this.historyMapper = historyMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public PersonnelResponse getPersonnelById(UUID id) {

        Personnel personnel = findPersonnelByIdOrThrow(id);
        return personnelMapper.toPersonnelResponse(personnel);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PersonnelResponse> getAllPersonnel() {

        List<Personnel> personnels = personnelRepository.findAll();
        return personnelMapper.toPersonnelResponseList(personnels);
    }

    @Override
    @Transactional
    public PersonnelResponse createPersonnel(PersonnelCreateRequest request) {

        personnelRepository.findByTcIdentityNumber(request.getTcIdentityNumber())
                .ifPresent(p -> {throw new DuplicateResourceException(ErrorCode.TCKN_ALREADY_EXISTS);});

        Personnel personnel = personnelMapper.toPersonnel(request);

        Department department = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.DEPARTMENT_NOT_FOUND, "Department not found with id: " + request.getDepartmentId()));
        Position position = positionRepository.findById(request.getPositionId()).orElseThrow(()->new ResourceNotFoundException(ErrorCode.POSITION_NOT_FOUND, "Position not found with id: " + request.getPositionId()));

        personnel.setDepartment(department);
        personnel.setPosition(position);

        if (request.getEducationLevelId() != null){

            EducationLevel educationLevel = educationLevelRepository.findById(request.getEducationLevelId()).orElseThrow(()->new ResourceNotFoundException(ErrorCode.EDUCATION_LEVEL_NOT_FOUND, "Education level not found with id: " + request.getEducationLevelId()));

            personnel.setEducationLevel(educationLevel);
        }

        Personnel savedPersonnel = personnelRepository.save(personnel);

        PersonnelEmploymentHistory initialHistory = new PersonnelEmploymentHistory();
        initialHistory.setPersonnel(savedPersonnel);
        initialHistory.setDepartment(savedPersonnel.getDepartment());
        initialHistory.setPosition(savedPersonnel.getPosition());
        initialHistory.setStartDate(LocalDate.now());
        historyRepository.save(initialHistory);

        return personnelMapper.toPersonnelResponse(savedPersonnel);
    }

    @Override
    @Transactional
    public PersonnelResponse updatePersonnel(UUID id, PersonnelUpdateRequest request) {

        Personnel personnel = findPersonnelByIdOrThrow(id);
        personnelMapper.updatePersonnelFromRequest(request, personnel);

        Department department = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.RESOURCE_NOT_FOUND, "Department not found with id: " + request.getDepartmentId()));
        Position position = positionRepository.findById(request.getPositionId()).orElseThrow(()->new ResourceNotFoundException(ErrorCode.RESOURCE_NOT_FOUND, "Position not found with id: " + request.getPositionId()));

        personnel.setDepartment(department);
        personnel.setPosition(position);

        if (request.getEducationLevelId() != null){

            EducationLevel educationLevel = educationLevelRepository.findById(request.getEducationLevelId()).orElseThrow(()->new ResourceNotFoundException(ErrorCode.RESOURCE_NOT_FOUND, "Education level not found with id: " + request.getEducationLevelId()));

            personnel.setEducationLevel(educationLevel);
        } else {
            personnel.setEducationLevel(null);
        }

        Personnel updatedPersonnel = personnelRepository.save(personnel);

        return personnelMapper.toPersonnelResponse(updatedPersonnel);
    }

    @Override
    @Transactional
    public void deletePersonnel(UUID id) {
        if (!personnelRepository.existsById(id)){
            throw new ResourceNotFoundException(ErrorCode.PERSONNEL_NOT_FOUND);
        }
        personnelRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void uploadProfilePhoto(UUID personnelId, MultipartFile file) {
        Personnel personnel = findPersonnelByIdOrThrow(personnelId);
        try {
            personnel.setProfilePhoto(file.getBytes());
            personnelRepository.save(personnel);
        }catch (Exception e){
            throw new RuntimeException("Could not read file data" + e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<PersonnelEmploymentHistoryResponse> getEmploymentHistory(UUID personnelId) {
        if (!personnelRepository.existsById(personnelId)) {
            throw new ResourceNotFoundException(ErrorCode.PERSONNEL_NOT_FOUND);
        }

        List<PersonnelEmploymentHistory> historyList = historyRepository.findByPersonnelId(personnelId);
        return historyMapper.toResponseList(historyList);
    }

    private Personnel findPersonnelByIdOrThrow(UUID id){
        return personnelRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException(ErrorCode.PERSONNEL_NOT_FOUND));
    }
}
