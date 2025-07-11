package com.mcyldz.hrinventory.dto.request;

import com.mcyldz.hrinventory.entity.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class PersonnelCreateRequest {

    @NotBlank(message = "First name cannot be blank")
    @Size(max = 50)
    private String firstName;

    @NotBlank(message = "Last name cannot be blank")
    @Size(max = 50)
    private String lastName;

    @NotBlank(message = "TC Identity Number cannot be blank")
    @Size(min = 11, max = 11, message = "TCKN must be 11 characters")
    private String tcIdentityNumber;

    @NotNull(message = "Gender cannot be null")
    private Gender gender;

    @NotNull(message = "Birth date cannot be null")
    @Past(message = "Birth date must be in the past")
    private LocalDate birthDate;

    @NotNull(message = "Marital status cannot be null")
    private MaritalStatus maritalStatus;

    private byte[] profilePhoto;

    private EducationLevel educationLevel;

    @NotNull(message = "Department ID cannot be null")
    private Department department;

    @NotNull(message = "Position ID cannot be null")
    private Position position;
}
