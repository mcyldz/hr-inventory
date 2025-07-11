package com.mcyldz.hrinventory.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.parameters.P;

import java.time.LocalDate;

@Entity
@Table(name = "personnel_employment_history")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PersonnelEmploymentHistory extends BaseEntity{

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "termination_reason", length = 500)
    private String terminationReason;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "personnel_id", nullable = false, referencedColumnName = "id")
    private Personnel personnel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "start_position_id", nullable = false, referencedColumnName = "id")
    private Position position;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "start_department_id", nullable = false, referencedColumnName = "id")
    private Department department;
}
