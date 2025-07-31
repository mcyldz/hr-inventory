package com.mcyldz.hrinventory.service;

import com.mcyldz.hrinventory.dto.request.PersonnelCreateRequest;
import com.mcyldz.hrinventory.dto.request.PersonnelUpdateRequest;
import com.mcyldz.hrinventory.dto.response.PersonnelEmploymentHistoryResponse;
import com.mcyldz.hrinventory.dto.response.PersonnelResponse;

import java.util.List;
import java.util.UUID;

public interface PersonnelService {

    PersonnelResponse getPersonnelById(UUID id);

    List<PersonnelResponse> getAllPersonnel();

    PersonnelResponse createPersonnel(PersonnelCreateRequest request);

    PersonnelResponse updatePersonnel(UUID id, PersonnelUpdateRequest request);

    void deletePersonnel(UUID id);

    List<PersonnelEmploymentHistoryResponse> getEmploymentHistory(UUID personnelId);
}
