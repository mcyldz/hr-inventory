package com.mcyldz.hrinventory.controller;

import com.mcyldz.hrinventory.dto.request.EducationLevelCreateRequest;
import com.mcyldz.hrinventory.dto.request.EducationLevelUpdateRequest;
import com.mcyldz.hrinventory.dto.response.ApiResponse;
import com.mcyldz.hrinventory.dto.response.EducationLevelResponse;
import com.mcyldz.hrinventory.service.EducationLevelService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/education-levels")
public class EducationLevelController {

    private final EducationLevelService educationLevelService;

    public EducationLevelController(EducationLevelService educationLevelService) {
        this.educationLevelService = educationLevelService;
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<EducationLevelResponse>> getEducationLevelById(@PathVariable UUID id) {
        EducationLevelResponse level = educationLevelService.getEducationLevelById(id);
        return ResponseEntity.ok(ApiResponse.success("Education level retrieved successfully", level));
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<List<EducationLevelResponse>>> getAllEducationLevels() {
        List<EducationLevelResponse> levels = educationLevelService.getAllEducationLevels();
        return ResponseEntity.ok(ApiResponse.success("All education levels retrieved successfully", levels));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<EducationLevelResponse>> createEducationLevel(@Valid @RequestBody EducationLevelCreateRequest request) {
        EducationLevelResponse createdLevel = educationLevelService.createEducationLevel(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Education level created successfully", createdLevel));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<EducationLevelResponse>> updateEducationLevel(
            @PathVariable UUID id,
            @Valid @RequestBody EducationLevelUpdateRequest request) {
        EducationLevelResponse updatedLevel = educationLevelService.updateEducationLevel(id, request);
        return ResponseEntity.ok(ApiResponse.success("Education level updated successfully", updatedLevel));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteEducationLevel(@PathVariable UUID id) {
        educationLevelService.deleteEducationLevel(id);
        return ResponseEntity.noContent().build();
    }

}
