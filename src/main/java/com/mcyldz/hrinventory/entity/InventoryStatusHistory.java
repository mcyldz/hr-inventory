package com.mcyldz.hrinventory.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;

@Entity
@Table(name = "inventory_status_history")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SQLDelete(sql = "UPDATE inventory_status_history SET is_deleted = true, last_modified_date = CURRENT_TIMESTAMP WHERE id = ?")
@SQLRestriction("is_deleted = false")
public class InventoryStatusHistory extends BaseEntity {

    @Column(name = "change_date", nullable = false)
    private LocalDateTime changeDate;

    @Column(name = "notes", length = 500)
    private String notes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inventory_item_id", nullable = false)
    private InventoryItem inventoryItem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_id", nullable = false)
    private InventoryStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "changed_by", nullable = false)
    private User changedBy;
}
