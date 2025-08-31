package com.mcyldz.hrinventory.entity;

import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.*;

import java.sql.Types;
import java.time.LocalDate;

@Entity
@Table(name = "personnel")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
@SQLDelete(sql = "UPDATE personnel SET is_deleted = true, last_modified_date = CURRENT_TIMESTAMP WHERE id = ?")
@SQLRestriction("is_deleted = false")
public class Personnel extends BaseEntity{

    @Column(name = "registry_number", nullable = false, unique = true, updatable = false)
    private Integer registryNumber;

    @Column(name = "first_name", length = 50, nullable = false)
    private String firstName;

    @Column(name = "last_name", length = 50, nullable = false)
    private String lastName;

    @Column(name = "tc_identity_number", length = 11, nullable = false, unique = true)
    private String tcIdentityNumber;

    @Column(name = "gender", length = 1, nullable = false)
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;

    @Column(name = "marital_status", length = 20, nullable = false)
    @Enumerated(EnumType.STRING)
    private MaritalStatus maritalStatus;

    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;

    @Lob
    @Column(name = "profile_photo")
    @JdbcTypeCode(Types.BINARY)
    private byte[] profilePhoto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "education_level_id", referencedColumnName = "id")
    private EducationLevel educationLevel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id", nullable = false, referencedColumnName = "id")
    private Department department;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "position_id", nullable = false, referencedColumnName = "id")
    private Position position;
}
