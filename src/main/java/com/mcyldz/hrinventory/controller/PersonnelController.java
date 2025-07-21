package com.mcyldz.hrinventory.controller;

import com.mcyldz.hrinventory.dto.request.PersonnelCreateRequest;
import com.mcyldz.hrinventory.dto.request.PersonnelUpdateRequest;
import com.mcyldz.hrinventory.dto.response.ApiResponse;
import com.mcyldz.hrinventory.dto.response.PersonnelResponse;
import com.mcyldz.hrinventory.service.PersonnelService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "/api/v1/personnel")
public class PersonnelController {

    private final PersonnelService personnelService;

    public PersonnelController(PersonnelService personnelService) {
        this.personnelService = personnelService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<PersonnelResponse>>> getAllPersonnel() {
        List<PersonnelResponse> personnelList = personnelService.getAllPersonnel();
        return ResponseEntity.ok(ApiResponse.success("All personnel retrieved successfully", personnelList));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PersonnelResponse>> getPersonnelById(@PathVariable UUID id) {
        PersonnelResponse personnel = personnelService.getPersonnelById(id);
        return ResponseEntity.ok(ApiResponse.success("Personnel retrieved successfully", personnel));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<PersonnelResponse>> createPersonnel(@Valid @RequestBody PersonnelCreateRequest request) {
        PersonnelResponse createdPersonnel = personnelService.createPersonnel(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Personnel created successfully", createdPersonnel));
    }

    @PostMapping("/{id}/photo")
    public ResponseEntity<ApiResponse<Void>> uploadProfilePhoto(@PathVariable UUID id,
            @RequestParam("file") MultipartFile file){

        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body(ApiResponse.error("File cannot be empty."));
        }
        personnelService.uploadProfilePhoto(id, file);
        return ResponseEntity.ok(ApiResponse.success("Profile photo uploaded successfully."));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<PersonnelResponse>> updatePersonnel(
            @PathVariable UUID id,
            @Valid @RequestBody PersonnelUpdateRequest request) {
        PersonnelResponse updatedPersonnel = personnelService.updatePersonnel(id, request);
        return ResponseEntity.ok(ApiResponse.success("Personnel updated successfully", updatedPersonnel));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePersonnel(@PathVariable UUID id) {
        personnelService.deletePersonnel(id);
        return ResponseEntity.noContent().build();
    }
}
