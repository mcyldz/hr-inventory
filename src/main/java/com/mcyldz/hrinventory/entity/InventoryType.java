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
@Table(name = "inventory_types")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SQLDelete(sql = "UPDATE inventory_types SET is_deleted = true, last_modified_date = CURRENT_TIMESTAMP WHERE id = ?")
@SQLRestriction("is_deleted = false")
public class InventoryType extends BaseEntity{

    @Column(name = "name", length = 50, nullable = false, unique = true)
    private String name;

    @Column(name = "description")
    private String description;
}
