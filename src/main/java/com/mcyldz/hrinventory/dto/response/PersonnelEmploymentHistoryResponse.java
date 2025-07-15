package com.mcyldz.hrinventory.dto.response;

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
public class PersonnelEmploymentHistoryResponse {

    private UUID id;

    private LocalDate startDate;

    private LocalDate endDate;

    private String terminationReason;

    private UUID personnelId;

    private String personnelFullName;

    private String positionName;

    private String departmentName;
}
