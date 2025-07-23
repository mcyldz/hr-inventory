package com.mcyldz.hrinventory.service;

import com.mcyldz.hrinventory.dto.request.AssignmentCreateRequest;
import com.mcyldz.hrinventory.dto.request.AssignmentReturnRequest;
import com.mcyldz.hrinventory.dto.response.AssignmentResponse;

import java.util.List;
import java.util.UUID;

public interface AssignmentService {

    List<AssignmentResponse> getAssignmentsByPersonnelId(UUID personnelId);

    List<AssignmentResponse> createAssignments(AssignmentCreateRequest request);

    List<AssignmentResponse> returnAssignments(AssignmentReturnRequest request);
}
