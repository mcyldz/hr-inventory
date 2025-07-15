package com.mcyldz.hrinventory.dto.response;

import com.mcyldz.hrinventory.entity.Gender;
import com.mcyldz.hrinventory.entity.MaritalStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PersonnelResponse {

    private UUID id;

    private Integer registryNumber;

    private String firstName;

    private String lastName;

    private String tcIdentityNumber;

    private Gender gender;

    private MaritalStatus maritalStatus;

    private LocalDate birthDate;

    private boolean isActive;

    private String educationLevelName;

    private String departmentName;

    private String positionName;
}
