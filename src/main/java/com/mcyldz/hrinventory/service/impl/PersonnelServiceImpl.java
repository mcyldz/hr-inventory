package com.mcyldz.hrinventory.service.impl;

import com.mcyldz.hrinventory.dto.request.PersonnelCreateRequest;
import com.mcyldz.hrinventory.dto.request.PersonnelUpdateRequest;
import com.mcyldz.hrinventory.dto.response.PersonnelEmploymentHistoryResponse;
import com.mcyldz.hrinventory.dto.response.PersonnelResponse;
import com.mcyldz.hrinventory.entity.*;
import com.mcyldz.hrinventory.exception.model.BusinessLogicException;
import com.mcyldz.hrinventory.exception.model.DuplicateResourceException;
import com.mcyldz.hrinventory.exception.model.ErrorCode;
import com.mcyldz.hrinventory.exception.model.ResourceNotFoundException;
import com.mcyldz.hrinventory.mapper.PersonnelEmploymentHistoryMapper;
import com.mcyldz.hrinventory.mapper.PersonnelMapper;
import com.mcyldz.hrinventory.repository.*;
import com.mcyldz.hrinventory.service.PersonnelService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class PersonnelServiceImpl implements PersonnelService {

    private final PersonnelRepository personnelRepository;

    private final EducationLevelRepository educationLevelRepository;

    private final DepartmentRepository departmentRepository;

    private final PositionRepository positionRepository;

    private final PersonnelEmploymentHistoryRepository historyRepository;

    private final PersonnelMapper personnelMapper;

    private final PersonnelEmploymentHistoryMapper historyMapper;

    private final PersonnelInventoryAssignmentRepository assignmentRepository;

    public PersonnelServiceImpl(PersonnelRepository personnelRepository, EducationLevelRepository educationLevelRepository, DepartmentRepository departmentRepository, PositionRepository positionRepository, PersonnelEmploymentHistoryRepository historyRepository, PersonnelMapper personnelMapper, PersonnelEmploymentHistoryMapper historyMapper, PersonnelInventoryAssignmentRepository assignmentRepository) {
        this.personnelRepository = personnelRepository;
        this.educationLevelRepository = educationLevelRepository;
        this.departmentRepository = departmentRepository;
        this.positionRepository = positionRepository;
        this.historyRepository = historyRepository;
        this.personnelMapper = personnelMapper;
        this.historyMapper = historyMapper;
        this.assignmentRepository = assignmentRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public PersonnelResponse getPersonnelById(UUID id) {

        Personnel personnel = findPersonnelByIdOrThrow(id);
        return personnelMapper.toPersonnelResponse(personnel);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PersonnelResponse> getAllPersonnel(Integer page, Integer size) {

        Pageable pageable = PageRequest.of(page, size);

        Page<Personnel> personnelPage = personnelRepository.findAll(pageable);

        List<Personnel> personnelList = personnelPage.getContent();

        return personnelMapper.toPersonnelResponseList(personnelList);
    }

    @Override
    @Transactional
    public PersonnelResponse createPersonnel(PersonnelCreateRequest request) {

        personnelRepository.findByTcIdentityNumber(request.getTcIdentityNumber())
                .ifPresent(p -> {throw new DuplicateResourceException(ErrorCode.TCKN_ALREADY_EXISTS);});

        Personnel personnel = personnelMapper.toPersonnel(request);

        Department department = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.DEPARTMENT_NOT_FOUND, "Department not found with id: " + request.getDepartmentId()));

        Position position = positionRepository.findById(request.getPositionId())
                .orElseThrow(()->new ResourceNotFoundException(ErrorCode.POSITION_NOT_FOUND, "Position not found with id: " + request.getPositionId()));

        personnel.setDepartment(department);
        personnel.setPosition(position);

        if (request.getEducationLevelId() != null){

            EducationLevel educationLevel = educationLevelRepository.findById(request.getEducationLevelId()).orElseThrow(()->new ResourceNotFoundException(ErrorCode.EDUCATION_LEVEL_NOT_FOUND, "Education level not found with id: " + request.getEducationLevelId()));

            personnel.setEducationLevel(educationLevel);
        }

        personnel.setRegistryNumber(generateUniqueRegistryNumber());
        Personnel savedPersonnel = personnelRepository.save(personnel);

        createEmploymentHistory(savedPersonnel, request.getStartDate());

        return personnelMapper.toPersonnelResponse(savedPersonnel);
    }

    @Override
    @Transactional
    public PersonnelResponse updatePersonnel(UUID id, PersonnelUpdateRequest request) {

        Personnel existPersonnel = findPersonnelByIdOrThrow(id);

        UUID oldDepartmantId = existPersonnel.getDepartment().getId();
        UUID oldPositionId = existPersonnel.getPosition().getId();
        boolean oldActive = existPersonnel.isActive();

        personnelMapper.updatePersonnelFromRequest(request, existPersonnel);

        Department department = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.DEPARTMENT_NOT_FOUND, "Department not found with id: " + request.getDepartmentId()));

        Position position = positionRepository.findById(request.getPositionId())
                .orElseThrow(()->new ResourceNotFoundException(ErrorCode.POSITION_NOT_FOUND, "Position not found with id: " + request.getPositionId()));

        existPersonnel.setDepartment(department);
        existPersonnel.setPosition(position);

        if (request.getEducationLevelId() != null){

            EducationLevel educationLevel = educationLevelRepository.findById(request.getEducationLevelId())
                    .orElseThrow(()->new ResourceNotFoundException(ErrorCode.EDUCATION_LEVEL_NOT_FOUND, "Education level not found with id: " + request.getEducationLevelId()));

            existPersonnel.setEducationLevel(educationLevel);
        }

        if (!request.getDepartmentId().equals(oldDepartmantId) || !request.getPositionId().equals(oldPositionId)){

            PersonnelEmploymentHistory employmentHistory = historyRepository.findByPersonnelIdAndEndDateIsNull(existPersonnel.getId())
                    .orElseThrow(()->new ResourceNotFoundException(ErrorCode.RESOURCE_NOT_FOUND));

            employmentHistory.setEndDate(LocalDate.now());
            historyRepository.save(employmentHistory);

            createEmploymentHistory(existPersonnel);
        }

        if (oldActive && !request.isActive()){

            if (assignmentRepository.existsByPersonnelIdAndReturnDateIsNull(existPersonnel.getId())){
                throw new BusinessLogicException(ErrorCode.PERSONNEL_HAS_ACTIVE_ASSIGNMENTS);
            }

            PersonnelEmploymentHistory employmentHistory = historyRepository.findByPersonnelIdAndEndDateIsNull(existPersonnel.getId())
                    .orElseThrow(()->new ResourceNotFoundException(ErrorCode.RESOURCE_NOT_FOUND));

            employmentHistory.setEndDate(LocalDate.now());
            employmentHistory.setTerminationReason(request.getTerminationReason());
            historyRepository.save(employmentHistory);
        }

        Personnel updatedPersonnel = personnelRepository.save(existPersonnel);

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

    private Integer generateUniqueRegistryNumber(){
        int min = 1_000_000;
        int max = 10_000_000;

        Integer registeryNumber;

        do {
            registeryNumber =ThreadLocalRandom.current().nextInt(min, max);
        } while (personnelRepository.existsByRegistryNumber(registeryNumber));

        return registeryNumber;
    }

    private void createEmploymentHistory(Personnel personnel, LocalDate startDate){
        PersonnelEmploymentHistory employmentHistory = new PersonnelEmploymentHistory();
        employmentHistory.setPersonnel(personnel);
        employmentHistory.setDepartment(personnel.getDepartment());
        employmentHistory.setPosition(personnel.getPosition());
        employmentHistory.setStartDate(startDate);
        historyRepository.save(employmentHistory);
    }

    private void createEmploymentHistory(Personnel personnel){
        this.createEmploymentHistory(personnel, LocalDate.now());
    }
}
