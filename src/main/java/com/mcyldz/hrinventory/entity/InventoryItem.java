package com.mcyldz.hrinventory.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDate;

@Entity
@Table(name = "inventory_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SQLDelete(sql = "UPDATE inventory_items SET is_deleted = true, last_modified_date = CURRENT_TIMESTAMP WHERE id = ?")
@SQLRestriction("is_deleted = false")
public class InventoryItem extends BaseEntity{

    @Column(name = "brand", length = 100, nullable = false)
    private String brand;

    @Column(name = "model", length = 100, nullable = false)
    private String model;

    @Column(name = "serial_number", length = 100, nullable = false, unique = true)
    private String serialNumber;

    @Column(name = "entry_date", nullable = false)
    private LocalDate entryDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inventory_type_id", nullable = false, referencedColumnName = "id")
    private InventoryType inventoryType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "current_status_id", nullable = false, referencedColumnName = "id")
    private InventoryStatus currentStatus;
}
