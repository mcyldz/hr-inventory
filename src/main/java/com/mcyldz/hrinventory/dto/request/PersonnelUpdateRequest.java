package com.mcyldz.hrinventory.dto.request;

import com.mcyldz.hrinventory.entity.Gender;
import com.mcyldz.hrinventory.entity.MaritalStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class PersonnelUpdateRequest {

    @NotBlank(message = "First name cannot be blank")
    @Size(max = 50)
    private String firstName;

    @NotBlank(message = "Last name cannot be blank")
    @Size(max = 50)
    private String lastName;

    @NotNull(message = "Gender cannot be null")
    private Gender gender;

    @NotNull(message = "Birth date cannot be null")
    @Past(message = "Birth date must be in the past")
    private LocalDate birthDate;

    @NotNull(message = "Marital status cannot be blank")
    private MaritalStatus maritalStatus;

    @NotNull(message = "Department ID cannot be null")
    private UUID departmentId;

    @NotNull(message = "Position ID cannot be null")
    private UUID positionId;

    private UUID educationLevelId;

    @NotNull(message = "Active status cannot be null")
    private boolean isActive;

    private String profilePhoto;

    private String terminationReason;
}
