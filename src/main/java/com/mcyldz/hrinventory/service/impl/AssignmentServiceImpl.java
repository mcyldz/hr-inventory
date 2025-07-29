package com.mcyldz.hrinventory.service.impl;

import com.mcyldz.hrinventory.dto.request.AssignmentCreateRequest;
import com.mcyldz.hrinventory.dto.request.AssignmentReturnRequest;
import com.mcyldz.hrinventory.dto.response.AssignmentResponse;
import com.mcyldz.hrinventory.entity.*;
import com.mcyldz.hrinventory.exception.model.BusinessLogicException;
import com.mcyldz.hrinventory.exception.model.ErrorCode;
import com.mcyldz.hrinventory.exception.model.ResourceNotFoundException;
import com.mcyldz.hrinventory.mapper.AssignmentMapper;
import com.mcyldz.hrinventory.repository.*;
import com.mcyldz.hrinventory.service.AssignmentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class AssignmentServiceImpl implements AssignmentService {

    private final PersonnelInventoryAssignmentRepository assignmentRepository;
    private final PersonnelRepository personnelRepository;
    private final InventoryItemRepository inventoryItemRepository;
    private final InventoryStatusRepository inventoryStatusRepository;
    private final InventoryStatusHistoryRepository inventoryStatusHistoryRepository;
    private final UserRepository userRepository;
    private final AssignmentMapper assignmentMapper;

    private static final String STATUS_IN_USE = "PERSONELDE";
    private static final String STATUS_IN_STOCK = "DEPODA";

    public AssignmentServiceImpl(PersonnelInventoryAssignmentRepository assignmentRepository, PersonnelRepository personnelRepository, InventoryItemRepository inventoryItemRepository, InventoryStatusRepository inventoryStatusRepository, InventoryStatusHistoryRepository inventoryStatusHistoryRepository, UserRepository userRepository, AssignmentMapper assignmentMapper) {
        this.assignmentRepository = assignmentRepository;
        this.personnelRepository = personnelRepository;
        this.inventoryItemRepository = inventoryItemRepository;
        this.inventoryStatusRepository = inventoryStatusRepository;
        this.inventoryStatusHistoryRepository = inventoryStatusHistoryRepository;
        this.userRepository = userRepository;
        this.assignmentMapper = assignmentMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<AssignmentResponse> getAssignmentsByPersonnelId(UUID personnelId) {
        if (!personnelRepository.existsById(personnelId)) {
            throw new ResourceNotFoundException(ErrorCode.PERSONNEL_NOT_FOUND);
        }

        List<PersonnelInventoryAssignment> assignments = assignmentRepository.findByPersonnelIdAndReturnDateIsNull(personnelId);
        return assignmentMapper.toAssignmentResponseList(assignments);
    }

    @Override
    @Transactional
    public List<AssignmentResponse> createAssignments(AssignmentCreateRequest request) {
        Personnel personnel = personnelRepository.findById(request.getPersonnelId())
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.PERSONNEL_NOT_FOUND));

        User currentUser = userRepository.findByUsername("admin").orElseThrow(() -> new ResourceNotFoundException(ErrorCode.USER_NOT_FOUND));

        InventoryStatus statusInUse = inventoryStatusRepository.findByName(STATUS_IN_USE)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.INVENTORY_STATUS_NOT_FOUND));

        List<PersonnelInventoryAssignment> newAssignments = new ArrayList<>();

        for (UUID itemId : request.getInventoryItemIds()) {
            InventoryItem item = inventoryItemRepository.findById(itemId)
                    .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.INVENTORY_ITEM_NOT_FOUND));

            if (assignmentRepository.existsByInventoryItemIdAndReturnDateIsNull(item.getId())) {
                throw new BusinessLogicException(ErrorCode.INVENTORY_ITEM_ALREADY_ASSIGNED);
            }

            PersonnelInventoryAssignment newAssignment = new PersonnelInventoryAssignment();
            newAssignment.setAssignedBy(currentUser);
            newAssignment.setAssignmentDate(request.getAssignmentDate());
            newAssignment.setPersonnel(personnel);
            newAssignment.setInventoryItem(item);
            newAssignment.setNotes(request.getNotes());

            item.setCurrentStatus(statusInUse);
            inventoryItemRepository.save(item);

            InventoryStatusHistory history = new InventoryStatusHistory();
            history.setInventoryItem(item);
            history.setStatus(statusInUse);
            history.setChangedBy(currentUser);
            inventoryStatusHistoryRepository.save(history);

            PersonnelInventoryAssignment savedAssignment = assignmentRepository.save(newAssignment);

            newAssignments.add(savedAssignment);
        }

        return assignmentMapper.toAssignmentResponseList(newAssignments);
    }

    @Override
    @Transactional
    public List<AssignmentResponse> returnAssignments(AssignmentReturnRequest request) {

        InventoryStatus statusInStock = inventoryStatusRepository.findByName(STATUS_IN_STOCK)
                .orElseThrow(() -> new IllegalStateException("Status 'DEPODA' not found in database."));

        User currentUser = userRepository.findByUsername("admin")
                .orElseThrow(() -> new IllegalStateException("Admin user not found for testing"));

        List<PersonnelInventoryAssignment> updatedAssignments = new ArrayList<>();

        for (UUID assignmentId  : request.getAssignmentIds()){

            PersonnelInventoryAssignment assignmentToReturn = assignmentRepository.findById(assignmentId)
                    .orElseThrow(() -> new ResourceNotFoundException(
                            ErrorCode.ASSIGNMENT_NOT_FOUND, "Assignment not found with id: " + assignmentId));

            if (assignmentToReturn.getReturnDate() != null) {
                continue;
            }

            assignmentToReturn.setReturnDate(request.getReturnDate());
            assignmentToReturn.setReturnedBy(currentUser);

            InventoryItem item = assignmentToReturn.getInventoryItem();
            if (item != null) {
                item.setCurrentStatus(statusInStock);
                inventoryItemRepository.save(item);

                InventoryStatusHistory history = new InventoryStatusHistory();
                history.setInventoryItem(item);
                history.setStatus(statusInStock);
                history.setChangedBy(currentUser);
                inventoryStatusHistoryRepository.save(history);
            }

            updatedAssignments.add(assignmentRepository.save(assignmentToReturn));
        }
        return assignmentMapper.toAssignmentResponseList(updatedAssignments);
    }
}
