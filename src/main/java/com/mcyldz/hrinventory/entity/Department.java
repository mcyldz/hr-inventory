package com.mcyldz.hrinventory.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "departments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SQLDelete(sql = "UPDATE departments SET is_deleted = true, last_modified_date = CURRENT_TIMESTAMP WHERE id = ?")
@SQLRestriction("is_deleted = false")
public class Department extends BaseEntity {

    @Column(name = "name", length = 100, nullable = false, unique = true)
    private String name;

    @Column(name = "description")
    private String description;
}
